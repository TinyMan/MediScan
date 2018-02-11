package prism.mediscan

import android.content.Context
import android.util.Log
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import org.funktionale.memoization.memoize
import prism.mediscan.model.Avis
import prism.mediscan.model.Presentation
import prism.mediscan.model.Specialite
import prism.mediscan.model.Substance

/**
 * Created by rapha on 14/01/2018.
 */

class BdpmDatabase constructor(context: Context) {

    private val DATABASE_NAME = "bdpm.sqlite"
    private val DATABASE_VERSION = 2

    private val TABLE_CIS_bdpm = "CIS_bdpm"
    private val TABLE_CIS_CIP_bdpm = "CIS_CIP_bdpm"
    private val TABLE_CIS_COMPO_bdpm = "CIS_COMPO_bdpm"
    private val TABLE_CIS_HAS_ASMR_bdpm = "CIS_HAS_ASMR_bdpm"
    private val TABLE_CIS_HAS_SMR_bdpm = "CIS_HAS_SMR_bdpm"
    private val TABLE_HAS_LiensPageCT_bdpm = "HAS_LiensPageCT_bdpm"
    private val TABLE_CIS_CPD_bdpm = "CIS_CPD_bdpm"
    private val TABLE_CIS_GENER_bdpm = "CIS_GENER_bdpm"

    private var database: SQLiteAssetHelper;
    private val memoizedGetPresentation = { cip: String -> this.internalGetPresentation(cip) }.memoize()

    init {
        this.database = SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
        this.database.setForcedUpgrade(2);

    }

    fun getPresentation(cip: String): Presentation? {
        return this.memoizedGetPresentation(cip)
    }

    private fun internalGetPresentation(cip: String): Presentation? {
        Log.d("BdpmDatabase", "GetPresentation " + cip)
        var column = "cip7";
        when (cip.length) {
            13 -> column = "cip13"
            7 -> column = "cip7"
            else -> throw Exception("Code CIP Invalide")
        }
        val query = "SELECT * FROM ${TABLE_CIS_CIP_bdpm} INNER JOIN ${TABLE_CIS_bdpm} ON ${TABLE_CIS_bdpm}.cis = ${TABLE_CIS_CIP_bdpm}.cis NATURAL LEFT OUTER JOIN ${TABLE_CIS_CPD_bdpm} WHERE ${column} = ?;"
        val q = this.database.readableDatabase.rawQuery(query, arrayOf(cip))
        if (q.moveToFirst()) {
            val cis = q.getString(q.getColumnIndex("cis"));
            val spe = Specialite(cis,
                    q.getString(q.getColumnIndex("nom")),
                    q.getString(q.getColumnIndex("formePharma")),
                    q.getString(q.getColumnIndex("statutAMM")),
                    q.getString(q.getColumnIndex("typeAMM")),
                    q.getString(q.getColumnIndex("etatCommercialisation")),
                    q.getString(q.getColumnIndex("dateAMM")),
                    q.getString(q.getColumnIndex("statutBDM")),
                    q.getString(q.getColumnIndex("numAutorisation")),
                    q.getInt(q.getColumnIndex("surveillance")),
                    q.getString(q.getColumnIndex("codeDocument")),
                    getListSubstance(cis),
                    q.getString(q.getColumnIndex("conditions")),
                    getListAvis(cis))
            val p = Presentation(q.getString(q.getColumnIndex("cip13")),
                    q.getString(q.getColumnIndex("cip7")),
                    spe,
                    q.getString(q.getColumnIndex("libelle")),
                    q.getString(q.getColumnIndex("statutAdministratif")),
                    q.getString(q.getColumnIndex("etatCommercialisation")),
                    q.getString(q.getColumnIndex("dateDeclaration")),
                    q.getString(q.getColumnIndex("agrementCollectivites")))
            q.close()
            return p;
        }
        q.close()
        return null;
    }

    private fun getListSubstance(cis: String): ArrayList<Substance> {
        Log.d("Database", "getListSubstance ( " + cis + " )")
        val query = "SELECT * FROM ${TABLE_CIS_COMPO_bdpm} WHERE ${TABLE_CIS_COMPO_bdpm}.cis = ?;"
        val q = this.database.readableDatabase.rawQuery(query, arrayOf(cis))
        val list = ArrayList<Substance>(q.count)
        while (q.moveToNext()) {
            val substance = Substance(
                    q.getString(q.getColumnIndex("nomSubstance")),
                    q.getInt(q.getColumnIndex("codeSubstance")),
                    q.getString(q.getColumnIndex("designation")),
                    q.getString(q.getColumnIndex("dosageSubstance")),
                    q.getString(q.getColumnIndex("refDosage")),
                    q.getString(q.getColumnIndex("nature")),
                    q.getInt(q.getColumnIndex("numLiaison"))
            )
            list.add(substance)
        }
        q.close()
        return list;
    }

    private fun getListAvis(cis: String): ArrayList<Avis> {
        val query = "SELECT motifEval, valeurASMR as titre, libelleASMR as libelle, dateAvisCT, codeHAS FROM ${TABLE_CIS_HAS_ASMR_bdpm} WHERE cis = ?" +
                "UNION SELECT motifEval, valeurSMR as titre, libelleSMR as libelle, dateAvisCT, codeHAS FROM ${TABLE_CIS_HAS_SMR_bdpm} WHERE cis = ?;"
        val q = this.database.readableDatabase.rawQuery(query, arrayOf(cis, cis));
        val list = ArrayList<Avis>(q.count);
        while (q.moveToNext()) {
            val avis = Avis(
                    q.getString(q.getColumnIndex("motifEval")),
                    q.getString(q.getColumnIndex("titre")),
                    q.getString(q.getColumnIndex("libelle")),
                    q.getString(q.getColumnIndex("dateAvisCT")),
                    q.getString(q.getColumnIndex("codeHAS"))
            )
            list.add(avis);
        }
        q.close();
        return list;
    }
}