package prism.mediscan

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_presentation_details.*
import kotlinx.android.synthetic.main.content_presentation_details.*
import prism.mediscan.model.Presentation


class PresentationDetails : AppCompatActivity() {

    var presentation = Presentation();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_details)
        setSupportActionBar(toolbar)
        val CIP = "3400930000649";


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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val code = result.contents
                try {
                    this.updateFromCip(code);
                } catch (e: Exception) {
                    this.showError(e);
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
