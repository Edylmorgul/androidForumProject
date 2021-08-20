package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class City implements Serializable {

    // Data
    private int id;
    private String nameCity;
    private int cp;
    private List<User> listUser; // Pourrait juste envoyer un objet utilisateur, dépend de la réalisation du programme

    // Constructor
    public City(){

    }

    public City(String nameCity, int cp){
        this.nameCity = nameCity;
        this.cp = cp;
        listUser = new LinkedList<User>();
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

    public int getCp(){return cp;}

    public void setCp(int cp){this.cp = cp;}

    public List<User> getListUser(){
        return listUser;
    }

    public void setListUser(List<User> listUser){
        this.listUser = listUser;
    }

    // Methods
    public List<City> readJsonList(JSONArray array) throws JSONException{
        List<City> list = new LinkedList<City>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            City city = new City();
            city.setId(obj.getInt("id"));
            city.setNameCity(obj.getString("nomVille"));
            city.setCp(obj.getInt("cp"));
            list.add(city);
        }
        return list;
    }

    public void readJson(JSONObject json)throws JSONException {
        this.setId(json.getInt("id"));
        this.setNameCity(json.getString("nomVille"));
        this.setCp(json.getInt("cp"));

        try{
            JSONArray array = json.getJSONArray("listeUtilisateur");
             User user = new User();
            this.setListUser(user.readJsonList(array));
        }
        catch (JSONException e){
            this.setListUser(null);
        }
    }
}
