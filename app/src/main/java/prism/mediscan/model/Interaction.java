package prism.mediscan.model;

/**
 * Created by joshua-hugo on 11/02/18.
 */

public class Interaction {
    private String description;
    private String type;
    private String conseil;

    public Interaction(String description, String type, String conseil){
        this.description = description;
        this.type = type;
        this.conseil = conseil;
    }

    public String getDescription(){
        return description;
    }

    public String getType(){
        return type;
    }

    public String getConseil(){
        return conseil;
    }
}
