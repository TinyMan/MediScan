package prism.mediscan.model

import java.io.Serializable

/**
 * Created by rapha on 29/01/2018.
 */
class Avis(val motif: String, val titre: String, val contenu: String, val date: String, val codeHAS: String) : Serializable {
}