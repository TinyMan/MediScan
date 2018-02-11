package prism.mediscan.model

import android.util.Log
import com.github.marlonlom.utilities.timeago.TimeAgo


/**
 * Created by rapha on 10/02/2018.
 */
open class Scan(val cip: String, val timestamp: Long, val expirationDate: String?, val numLot: String?) {
    val timeAgo: String

    init {
        Log.d("Scan ctor", timestamp.toString())
        timeAgo = TimeAgo.using(timestamp)
    }
}