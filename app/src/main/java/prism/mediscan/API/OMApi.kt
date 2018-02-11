package prism.mediscan.API

import android.content.Context
import android.util.Log
import com.google.gson.JsonElement
import com.koushikdutta.ion.Ion
import org.funktionale.memoization.memoize
import prism.mediscan.model.Interaction

/**
 * Created by rapha on 11/02/2018.
 */

fun stringOrDefault(el: JsonElement, def: String = ""): String {
    return if (el.isJsonNull) def else el.asString
}

private fun getInteractions(ctx: Context, cis1: String, cis2: String): List<Interaction> {
    val list = ArrayList<Interaction>()

    val json = Ion.with(ctx)
            .load("https://www.open-medicaments.fr/api/v1/interactions?ids=" + cis1 + "|" + cis2)
            .asJsonArray().get()

    for (it in json) {
        val interaction = Interaction(
                stringOrDefault(it.asJsonObject.get("description")),
                stringOrDefault(it.asJsonObject.get("type")),
                stringOrDefault(it.asJsonObject.get("conseil")),
                stringOrDefault(it.asJsonObject.get("codeCISMedicament1")),
                stringOrDefault(it.asJsonObject.get("codeCISMedicament2")),
                stringOrDefault(it.asJsonObject.get("nomMedicament1")),
                stringOrDefault(it.asJsonObject.get("nomMedicament2"))
        )
        list.add(interaction)
    }

    return list
}

private val memoizedGetInteractions = { a: Context, b: String, c: String -> getInteractions(a, b, c) }.memoize()

fun getInteractionsWithHistory(ctx: Context, cis: String, history: List<String>): List<Interaction> {
    Log.d("InteractionsWithHistory", cis + " -> " + history.joinToString())
    val list = HashSet<String>(history)
            .flatMap { hi ->
                val lo = hi < cis
                val cis1 = if (lo) hi else cis
                val cis2 = if (lo) cis else hi
                memoizedGetInteractions(ctx, cis1, cis2)
            }

    return list
}