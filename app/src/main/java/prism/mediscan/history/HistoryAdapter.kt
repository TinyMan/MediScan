package prism.mediscan.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import prism.mediscan.R

/**
 * Created by rapha on 10/02/2018.
 */
class HistoryAdapter(context: Context?, val resource: Int, val values: List<ScanListItem>?) : ArrayAdapter<ScanListItem>(context, resource, values) {

    override fun getItem(position: Int): ScanListItem {
        return super.getItem(count - position - 1)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val scan = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        view.findViewById<TextView>(R.id.dateScan).text = scan.timeAgo
        val nom = scan.presentation?.specialite?.nom;
        if (nom != null) {
            val list = nom.toLowerCase().split(",", limit = 2)
                    .map { str -> str.trim() }
                    .map { str -> str.capitalize() }

            view.findViewById<TextView>(R.id.nomSpecialite).text = list.firstOrNull()
            view.findViewById<TextView>(R.id.nomSpecialite2).text = list.getOrNull(1)
        }

        return view
    }
}