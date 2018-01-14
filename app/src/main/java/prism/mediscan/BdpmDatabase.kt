package prism.mediscan

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import prism.mediscan.model.Presentation
import prism.mediscan.model.Specialite

/**
 * Created by rapha on 14/01/2018.
 */

class BdpmDatabase constructor(context: Context) {

    private val DATABASE_NAME = "bdpm.sqlite"
    private val DATABASE_VERSION = 1

    private val TABLE_CIS_bdpm = "CIS_bdpm"
    private val TABLE_CIS_CIP_bdpm = "CIS_CIP_bdpm"
    private val TABLE_CIS_COMPO_bdpm = "CIS_COMPO_bdpm"
    private val TABLE_CIS_HAS_ASMR_bdpm = "CIS_HAS_ASMR_bdpm"
    private val TABLE_CIS_HAS_SMR_bdpm = "CIS_HAS_SMR_bdpm"
    private val TABLE_HAS_LiensPageCT_bdpm = "HAS_LiensPageCT_bdpm"
    private val TABLE_CIS_CPD_bdpm = "CIS_CPD_bdpm"
    private val TABLE_CIS_GENER_bdpm = "CIS_GENER_bdpm"

    private var database: SQLiteAssetHelper;

    init {
       this.database = SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
    }

    fun getPresentation(cip: String): Presentation? {
        var column = "cip7";
        when (cip.length) {
            13 -> column = "cip13"
            7 -> column = "cip7"
            else -> throw Exception("Code CIP Invalide")
        }
        val query = "SELECT * FROM ${TABLE_CIS_CIP_bdpm} INNER JOIN ${TABLE_CIS_bdpm} ON ${TABLE_CIS_bdpm}.cis = ${TABLE_CIS_CIP_bdpm}.cis WHERE ${column} = ?;"
        val q = this.database.readableDatabase.rawQuery(query, arrayOf(cip))
        if (q.moveToFirst()) {
            val spe = Specialite(q.getString(q.getColumnIndex("cis")),
                    q.getString(q.getColumnIndex("nom")),
                    q.getString(q.getColumnIndex("formePharma")),
                    q.getString(q.getColumnIndex("statutAMM")),
                    q.getString(q.getColumnIndex("typeAMM")),
                    q.getString(q.getColumnIndex("etatCommercialisation")),
                    q.getString(q.getColumnIndex("dateAMM")),
                    q.getString(q.getColumnIndex("statutBDM")),
                    q.getString(q.getColumnIndex("numAutorisation")),
                    q.getInt(q.getColumnIndex("surveillance")))
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
}