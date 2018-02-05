package prism.mediscan

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import prism.mediscan.model.Avis

/**
 * Created by rapha on 24/01/2018.
 */
class AvisAdapter(context: Context?, val resource: Int) : ArrayAdapter<Avis>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val avis = this.getItem(position);

        val view = convertView
                ?: LayoutInflater.from(getContext()).inflate(resource, parent, false)


        view.findViewById<TextView>(R.id.titreAvis).text = avis.titre
        view.findViewById<TextView>(R.id.dateAvis).text = avis.date;
        view.findViewById<TextView>(R.id.avisContenu).text = avis.contenu;

        view.setOnClickListener { view ->
            val v = view.findViewById<TextView>(R.id.avisContenu)
            v.visibility = if (v.visibility == View.GONE) View.VISIBLE else View.GONE;
            Log.d("Expand", "Expanding avis, list = " + parent?.toString());

            if (parent != null) {
                val listView = parent as ListView;
                val listAdapter = this
                val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED)
                var totalHeight = 0
                var view: View? = null
                for (i in 0 until listAdapter.getCount()) {
                    view = listAdapter.getView(i, view, listView)
                    if (i == 0)
                        view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT)

                    view!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += view.measuredHeight
                }
                val params = listView.getLayoutParams()
                params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
                listView.setLayoutParams(params)
            }
        }


        return view;
    }

}