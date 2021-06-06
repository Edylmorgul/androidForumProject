package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Ville implements Serializable {

    // Data
    private int id;
    private String nameCity;
    private List<Utilisateur> listUser; // Pourrait juste envoyer un objet utilisateur, dépend de la réalisation du programme

    // Constructor
    public Ville(){

    }

    public Ville(String nameCity){
        this.nameCity = nameCity;
        listUser = new LinkedList<Utilisateur>();
    }

    // GET/SET
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNameCity(){
        return nameCity;
    }

    public void setNameCity(String nameCity){
        this.nameCity = nameCity;
    }

    public List<Utilisateur> getListUser(){
        return listUser;
    }

    public void setListUser(List<Utilisateur> listUser){
        this.listUser = listUser;
    }

    // Methods
    public List<Ville> readJsonList(JSONArray array) throws JSONException{
        List<Ville> list = new LinkedList<Ville>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Ville city = new Ville();
            city.setId(obj.getInt("id"));
            city.setNameCity(obj.getString("nomVille"));
            list.add(city);
        }
        return list;
    }

    public void readJson(JSONObject json)throws JSONException {
        this.setId(json.getInt("id"));
        this.setNameCity(json.getString("nomVille"));

        try{
            JSONArray array = json.getJSONArray("listeUtilisateur");
             Utilisateur user = new Utilisateur();
            this.setListUser(user.readJsonList(array));
        }
        catch (JSONException e){
            this.setListUser(null);
        }
    }
}
