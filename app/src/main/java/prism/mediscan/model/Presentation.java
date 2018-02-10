package prism.mediscan.model;

import java.io.Serializable;

/**
 * Created by rapha on 13/01/2018.
 */

public class Presentation implements Serializable {
    public enum AgrementCollectivites {
        OUI,
        NON,
        INCONNU
    }

    private String cip13;
    private String cip7;
    private Specialite specialite;
    private String libelle;
    private String statutAdministratif;
    private String etatCommercialisation;
    private String datedeclaration;
    private AgrementCollectivites agrementCollectivites;
    private String lot;
    private String dateExp;

    public Presentation() {

    }

    public Presentation(String cip13, String cip7, Specialite specialite, String libelle, String statutAdministratif, String etatCommercialisation, String datedeclaration, String agrementCollectivites) {
        this.cip13 = cip13;
        this.cip7 = cip7;
        this.specialite = specialite;
        this.libelle = libelle;
        this.statutAdministratif = statutAdministratif;
        this.etatCommercialisation = etatCommercialisation;
        this.datedeclaration = datedeclaration;
        this.agrementCollectivites = AgrementCollectivites.valueOf(agrementCollectivites.toUpperCase());
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public void setDateExp(String dateExp) {
        this.dateExp = dateExp;
    }

    public String getLot() {
        return lot;
    }

    public String getDateExp() {
        return dateExp;
    }

    public String getCip13() {
        return cip13;
    }

    public String getCip7() {
        return cip7;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getStatutAdministratif() {
        return statutAdministratif;
    }

    public String getEtatCommercialisation() {
        return etatCommercialisation;
    }

    public String getDatedeclaration() {
        return datedeclaration;
    }

    public AgrementCollectivites getAgrementCollectivites() {
        return agrementCollectivites;
    }

}
