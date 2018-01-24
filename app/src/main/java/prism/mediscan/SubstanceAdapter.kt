package prism.mediscan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import prism.mediscan.model.Substance

/**
 * Created by rapha on 24/01/2018.
 */
class SubstanceAdapter(context: Context?, resource: Int) : ArrayAdapter<Substance>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val substance = this.getItem(position);

        val view = convertView
                ?: LayoutInflater.from(getContext()).inflate(R.layout.substance_layout, parent, false)


        view.findViewById<TextView>(R.id.nomSubstance).text = substance.nomSubstance

        return view;
    }

}