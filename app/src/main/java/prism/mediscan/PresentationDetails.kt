package prism.mediscan

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_presentation_details.*
import kotlinx.android.synthetic.main.content_presentation_details.*
import prism.mediscan.model.Presentation


class PresentationDetails : AppCompatActivity() {

    /**** Method for Setting the Height of the ListView dynamically.
     * Hack to fix the issue of not showing all the items of the ListView
     * when placed inside a ScrollView   */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.getAdapter() ?: return

        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.getCount()) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT)

            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.getLayoutParams()
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
        listView.setLayoutParams(params)
    }

    var presentation = Presentation();
    var substance_adapter: SubstanceAdapter? = null;
    var avis_adapter: AvisAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_details)
        setSupportActionBar(toolbar)
        val CIP = "3400930986141";

        substance_adapter = SubstanceAdapter(this, R.layout.substance_layout)
        liste_substances.setAdapter(substance_adapter)

        avis_adapter = AvisAdapter(this, R.layout.avis_layout)
        liste_avis.setAdapter(avis_adapter)


        liste_interactions.emptyView = empty_interactions
        liste_substances.emptyView = empty_substances
        liste_avis.emptyView = empty_avis

        this.updateFromCip(CIP);
    }

    fun startScan(view: View) {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.captureActivity = ScanActivity::class.java
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }

    fun voirNotice(view: View) {
        this.startDocumentActivity('N', this.presentation.specialite.codeDocument);
    }

    fun voirRCP(view: View) {
        this.startDocumentActivity('R', this.presentation.specialite.codeDocument);
    }

    fun startDocumentActivity(type: Char, codeDocument: String) {
        val intent = Intent(this, DocumentActivity::class.java);
        val b = Bundle();
        b.putChar("type", type);
        b.putString("code", codeDocument);
        intent.putExtras(b)
        startActivity(intent)
    }

    fun updateFromCip(cip: String) {
        val db = BdpmDatabase(this);
        val p = db.getPresentation(cip);
        if (p == null) throw Exception("Médicament inconnu")
        this.updateFromPresentation(p)
    }

    fun updateFromPresentation(p: Presentation) {
        this.presentation = p;
        libellePresentation.text = p.libelle
        formePharma.text = p.specialite.formePharmacologique;
        nomSpecialite.text = p.specialite.nom;
        conditionPrescription.text = p.specialite.cpd;
        substance_adapter?.clear()
        substance_adapter?.addAll(p.specialite.substances)
        if (p.specialite.substances.size > 0)
            refDosage.text = resources.getString(R.string.refDosage, p.specialite.substances.first().refDosage)

        avis_adapter?.clear();
        avis_adapter?.addAll(p.specialite.avis);
        if (p.specialite.avis.size > 0)
            dossierHAS.text = resources.getString(R.string.dossier_has, p.specialite.avis.first().codeHAS)

        setListViewHeightBasedOnChildren(liste_substances);
        setListViewHeightBasedOnChildren(liste_avis);
        setListViewHeightBasedOnChildren(liste_interactions);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                try {
                    var code = result.contents;
                    // format du datamatrix page 7: http://www.ucdcip.org/pdf/CIP-ACL%20cahier%20n%C2%B01%20Data%20Matrix%20Tra%C3%A7abilit%C3%A9.pdf
                    /** 01 03400930000120 17 AAMMJJ 10 A11111
                     * sans espace
                     * Se lit
                     * 01 = 0 + code CIP13
                     * 17 = date de péremption
                     * 10 = numéro de lot
                     */
                    val regex = Regex("^.010(\\d{13})17(\\d{2})(\\d{2})(\\d{2})10([A-Z0-9]+)$")
                    if (regex.matches(result.contents)) {
                        val matched = regex.matchEntire(result.contents)
                        code = matched?.groups?.get(1)?.value;
                        val annee = matched?.groups?.get(2)?.value
                        val mois = matched?.groups?.get(3)?.value
                        val jour = matched?.groups?.get(4)?.value
                        dateExpiration.text = "$mois/20$annee";
                        lot.text = matched?.groups?.get(5)?.value;
                    }
                    if (code != null)
                        this.updateFromCip(code)
                } catch (e: Exception) {
                    this.showError(e);
                    libellePresentation.text = result.contents
                    formePharma.text = result.contents.length.toString()

                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showError(e: Exception) {
        Toast.makeText(this, "Erreur: " + e.message, Toast.LENGTH_LONG).show();
    }

}
