package prism.mediscan.model

import java.io.Serializable

/**
 * Created by rapha on 24/01/2018.
 */
class Substance(val nomSubstance: String,
                val codeSubstance: Int,
                val designation: String,
                val dosageSubstance: String,
                val refDosage: String,
                val nature: String,
                val numLiaison: Int) : Serializable {

    override fun toString(): String {
        return nomSubstance
    }

}