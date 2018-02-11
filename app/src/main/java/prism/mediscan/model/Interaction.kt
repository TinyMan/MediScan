package prism.mediscan.model

import java.io.Serializable

/**
 * Created by rapha on 11/02/2018.
 */
data class Interaction(val description: String, val type: String, val conseil: String, val cis1: String, val cis2: String, val nomMedicament1: String, val nomMedicament2: String) : Serializable
