package prism.mediscan.API;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;


import org.json.JSONObject;

import prism.mediscan.model.Interaction;

/**
 * Created by joshua-hugo on 11/02/18.
 */

public class OMAPI {
    private String cis;

    public OMAPI(String cis){
        this.cis = cis;
    }

    public ArrayList<Interaction> getInteractions(String cis, ArrayList<String> cisList, Context context){
        ArrayList<Interaction> interactions = new ArrayList<Interaction>();

        if(cisList.size() == 0){
            return interactions;
        }
        else{
            for(int i = 0; i < cisList.size(); i++){
                try {
                    JsonObject json = Ion.with(context)
                            .load("https://www.open-medicaments.fr/api/v1/interactions?ids=" + cis + "|" + cisList.get(i))
                            .asJsonObject().get();

                    json.get("0");
                    Interaction interaction = new Interaction(json.get("description").getAsString(),
                            json.get("type").getAsString(),
                            json.get("conseil").getAsString());
                    interactions.add(interaction);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return interactions;
        }
    }
}
