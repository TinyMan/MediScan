package prism.mediscan.details

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter
import kotlinx.android.synthetic.main.activity_presentation_details.*
import kotlinx.android.synthetic.main.card_avis.*
import kotlinx.android.synthetic.main.card_interactions.*
import kotlinx.android.synthetic.main.card_resume.*
import kotlinx.android.synthetic.main.card_substances.*
import kotlinx.android.synthetic.main.content_presentation_details.*
import prism.mediscan.PRESENTATION
import prism.mediscan.R
import prism.mediscan.model.Presentation
import prism.mediscan.setListViewHeightBasedOnChildren
import java.util.*


class PresentationDetails : AppCompatActivity() {

    var presentation: Presentation? = null;
    var substance_adapter: SubstanceAdapter? = null;
    var avis_adapter: AvisExpandableAdapter? = null;
    var interaction_adapter: InteractionExpandableAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_details)
        setSupportActionBar(toolbar)


        substance_adapter = SubstanceAdapter(this, R.layout.substance_layout)
        liste_substances.setAdapter(substance_adapter)

//       liste_interactions.emptyView = empty_interactions
        liste_substances.emptyView = empty_substances
//        liste_avis.emptyView = empty_avis

        this.updateFromPresentation(intent.getSerializableExtra(PRESENTATION) as Presentation)
    }

    fun voirNotice(view: View) {
        this.startDocumentActivity('N', this.presentation?.specialite?.codeDocument);
    }

    fun voirRCP(view: View) {
        this.startDocumentActivity('R', this.presentation?.specialite?.codeDocument);
    }

    fun startDocumentActivity(type: Char, codeDocument: String?) {
        if (codeDocument != null) {
            val intent = Intent(this, DocumentActivity::class.java);
            val b = Bundle();
            b.putChar("type", type);
            b.putString("code", codeDocument);
            intent.putExtras(b)
            startActivity(intent)
        }
    }

    fun updateFromPresentation(p: Presentation) {
        this.presentation = p;
        title = p.specialite.nom

        // resume
        libellePresentation.text = p.libelle
        formePharma.text = p.specialite.formePharmacologique;
        nomSpecialite.text = p.specialite.nom;
        conditionPrescription.text = p.specialite.cpd;
        dateExpiration.text = if (p.dateExp != null) p.dateExp else ""
        lot.text = if (p.lot != null) p.lot else ""
        labelLot.visibility = if (p.lot != null) VISIBLE else GONE
        if (p.dateExp != null) {
            labelExp.visibility = VISIBLE
            val regex = Regex("(\\d{2})/(\\d{4})")

            if (regex.matches(p.dateExp)) {
                val matched = regex.matchEntire(p.dateExp)
                if (matched != null) {
                    val month = matched.groups.get(1)!!.value.toInt(10) - 1
                    val year = matched.groups.get(2)!!.value.toInt(10)
                    if (Calendar.getInstance().get(Calendar.YEAR) >= year && Calendar.getInstance().get(Calendar.MONTH) > month) {
                        val snack = Snackbar.make(coordinator, "Attention, ce médicament est périmé !", Snackbar.LENGTH_INDEFINITE)
                        snack.setAction("J'ai compris", { snack.dismiss() })
                                .setActionTextColor(Color.YELLOW)
                        snack.show();
                    }
                }
            }
        } else {
            labelExp.visibility = GONE
        }
        // substances
        substance_adapter?.clear()
        substance_adapter?.addAll(p.specialite.substances)
        if (p.specialite.substances.size > 0)
            refDosage.text = resources.getString(R.string.refDosage, p.specialite.substances.first().refDosage)

        if (p.specialite.avis.size > 0)
            dossierHAS.text = resources.getString(R.string.dossier_has, p.specialite.avis.first().codeHAS)

        // avis
        if (p.specialite.avis.size == 0) {
            dossierHAS.visibility = GONE
            empty_avis.visibility = VISIBLE
        } else {
            empty_avis.visibility = GONE
            dossierHAS.visibility = VISIBLE
        }
        avis_adapter = AvisExpandableAdapter(this, p.specialite)
        avis_adapter?.mode = ExpandableRecyclerAdapter.MODE_ACCORDION;
        avis_recycler.layoutManager = LinearLayoutManager(this)
        avis_recycler.setAdapter(avis_adapter)

        // interaction
        if (p.specialite.interactions.size == 0) {
            empty_interactions.visibility = VISIBLE
        } else {
            empty_interactions.visibility = GONE
        }
        interaction_adapter = InteractionExpandableAdapter(this, p.specialite)
        interaction_adapter?.mode = ExpandableRecyclerAdapter.MODE_ACCORDION;
        interaction_recycler.layoutManager = LinearLayoutManager(this);
        interaction_recycler.setAdapter(interaction_adapter);

        setListViewHeightBasedOnChildren(liste_substances);
//        setListViewHeightBasedOnChildren(liste_avis);
//        setListViewHeightBasedOnChildren(liste_interactions);
    }


}
