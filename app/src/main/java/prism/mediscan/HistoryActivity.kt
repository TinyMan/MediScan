package prism.mediscan

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import prism.mediscan.model.Presentation
import prism.mediscan.model.Scan

class HistoryActivity : AppCompatActivity() {
    var database: Database? = null
    var bdpm: BdpmDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        setTitle(resources.getString(R.string.title_activity_history))

        database = Database(this)
        bdpm = BdpmDatabase(this)

        val values = database?.getAllScans();
        scan_history.adapter = HistoryAdapter(this, R.layout.scan_layout, values)
        Log.d("HistoryActivity", "Hey")
    }


    fun goToDetails(presentation: Presentation) {
        try {
            val intent = Intent(this, PresentationDetails::class.java)
            intent.putExtra(PRESENTATION, presentation)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("HistoryActivity", "Error trying to go to details", e);
        }
    }


    fun storeScan(presentation: Presentation) {
        val scan = database?.storeScan(Scan(presentation.cip13, System.currentTimeMillis()))
        (scan_history.adapter as HistoryAdapter).add(scan)
    }

    fun onScanSuccessful(presentation: Presentation) {
        this.storeScan(presentation);
        this.goToDetails(presentation);
    }

    fun getPresentationFromCip(cip: String): Presentation {
        val p = bdpm?.getPresentation(cip);
        if (p == null) throw Exception("Médicament inconnu")
        return p;
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                try {
                    var code = result.contents;
                    var presentation: Presentation? = null;
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
                        if (code != null) {
                            presentation = this.getPresentationFromCip(code);
                            presentation.dateExp = "$mois/20$annee";
                            presentation.lot = matched?.groups?.get(5)?.value;
                        }
                    } else if (code != null)
                        presentation = this.getPresentationFromCip(code);
                    if (presentation != null)
                        this.onScanSuccessful(presentation);
                } catch (e: Exception) {
                    this.showError(e);
                    Log.e("MainActivity", "Parse scan result error: " + result.contents);
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
