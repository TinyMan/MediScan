package prism.mediscan.model;

/**
 * Created by rapha on 13/01/2018.
 */

public class Presentation {
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
