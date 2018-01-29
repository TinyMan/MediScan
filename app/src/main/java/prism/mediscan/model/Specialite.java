package prism.mediscan.model;

import java.util.ArrayList;

/**
 * Created by rapha on 13/01/2018.
 */

public class Specialite {
    private String cis;
    private String nom;
    private String formePharmacologique;
    private String statutAMM;
    private String typeAMM;
    private String etatCommercialisation;
    private String dateAMM;
    private String statutBDM;
    private String numAutorisation;
    private boolean surveillance;
    private String codeDocument;
    private String cpd;
    private ArrayList<Avis> avis;

    private ArrayList<Substance> substances;

    public Specialite(String cis, String nom, String formePharmacologique, String statutAMM, String typeAMM, String etatCommercialisation, String dateAMM, String statutBDM, String numAutorisation, int surveillance, String codeDocument, ArrayList<Substance> substances, String cpd, ArrayList<Avis> avis) {
        this.cis = cis;
        this.nom = nom;
        this.formePharmacologique = formePharmacologique;
        this.statutAMM = statutAMM;
        this.typeAMM = typeAMM;
        this.etatCommercialisation = etatCommercialisation;
        this.dateAMM = dateAMM;
        this.statutBDM = statutBDM;
        this.numAutorisation = numAutorisation;
        this.surveillance = surveillance == 1;
        this.codeDocument = codeDocument;
        this.substances = substances;
        this.cpd = cpd;
        this.avis = avis;
    }

    public ArrayList<Avis> getAvis() {
        return avis;
    }

    public String getCpd() {
        return cpd;
    }

    public ArrayList<Substance> getSubstances() {
        return substances;
    }

    public String getCis() {
        return cis;
    }

    public String getNom() {
        return nom;
    }

    public String getFormePharmacologique() {
        return formePharmacologique;
    }

    public String getStatutAMM() {
        return statutAMM;
    }

    public String getTypeAMM() {
        return typeAMM;
    }

    public String getEtatCommercialisation() {
        return etatCommercialisation;
    }

    public String getDateAMM() {
        return dateAMM;
    }

    public String getStatutBDM() {
        return statutBDM;
    }

    public String getNumAutorisation() {
        return numAutorisation;
    }

    public boolean isSurveillance() {
        return surveillance;
    }

    public String getCodeDocument() {
        return codeDocument;
    }
}
