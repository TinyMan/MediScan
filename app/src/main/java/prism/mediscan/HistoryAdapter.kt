package prism.mediscan

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import prism.mediscan.model.Scan

/**
 * Created by rapha on 10/02/2018.
 */
class HistoryAdapter(context: Context?, val resource: Int, val values: List<Scan>?) : ArrayAdapter<Scan>(context, resource, values) {

    override fun getItem(position: Int): Scan {
        return super.getItem(count - position - 1)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val scan = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        Log.d("HistoryAdapter", scan.cip)
        view.findViewById<TextView>(R.id.dateScan).text = scan.timeAgo
        view.findViewById<TextView>(R.id.nomSpecialite).text = scan.cip;

        return view
    }
}