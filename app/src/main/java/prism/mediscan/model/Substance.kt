package prism.mediscan.model

/**
 * Created by rapha on 24/01/2018.
 */
class Substance(nomSubstance: String, codeSubstance: Int, designation: String, dosageSubstance: String, refDosage: String, nature: String, numLiaison: Int) {
    val refDosage = refDosage;
    val nomSubstance = nomSubstance;
    val codeSubstance = codeSubstance;
    val designation = designation;
    val dosageSubstance = dosageSubstance;
    val nature = nature
    val numLiaison = numLiaison

}