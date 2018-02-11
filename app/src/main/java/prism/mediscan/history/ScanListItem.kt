package prism.mediscan.history

import prism.mediscan.BdpmDatabase
import prism.mediscan.model.Presentation
import prism.mediscan.model.Scan

/**
 * Created by rapha on 10/02/2018.
 */
class ScanListItem(bdpm: BdpmDatabase, scan: Scan) : Scan(scan.cip, scan.timestamp, scan.expirationDate, scan.numLot) {
    val presentation: Presentation?

    init {
        presentation = bdpm.getPresentation(scan.cip)
        presentation?.dateExp = scan.expirationDate
        presentation?.lot = scan.numLot
    }
}