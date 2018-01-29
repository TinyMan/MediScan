package prism.mediscan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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


        return view;
    }

}