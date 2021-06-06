package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Utilisateur implements Serializable {

    // Data
    private int id;
    private String pseudo;
    private String password;
    private String sex;
    private List<Sujet> listSubject;
    private List<Message> listMessage;

    // Constructor
    public Utilisateur(){

    }

    public Utilisateur(String pseudo, String password){
        this.pseudo = pseudo;
        this.password = password;
        listSubject = new LinkedList<Sujet>();
        listMessage = new LinkedList<Message>();
    }

    public Utilisateur(String pseudo, String password, String sex){
        this.pseudo = pseudo;
        this.password = password;
        this.sex = sex;
        listSubject = new LinkedList<Sujet>();
        listMessage = new LinkedList<Message>();
    }

    //GET/SET
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPseudo(){
        return pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getSex(){
        return sex;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public List<Sujet> getListSujet(){
        return listSubject;
    }

    public void setListSubject(List<Sujet> listSubject){
        this.listSubject = listSubject;
    }

    public List<Message> getListMessage(){
        return listMessage;
    }

    public void setListMessage(List<Message> listMessage){
        this.listMessage = listMessage;
    }

    // Methods
    public List<Utilisateur> readJsonList(JSONArray array) throws JSONException{
        List<Utilisateur> list = new LinkedList<Utilisateur>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Utilisateur user = new Utilisateur();
            user.setId(obj.getInt("id"));
            user.setPseudo(obj.getString("pseudo"));
            user.setPassword(obj.getString("motDePasse"));
            user.setSex(obj.getString("sexe"));
            try{
                JSONArray listSubject = obj.getJSONArray("listeSujet");
                Sujet subject = new Sujet();
                user.setListSubject(subject.readJsonList(listSubject));
            }
            catch (JSONException e){
                user.setListSubject(null);
            }
            try{
                JSONArray listMessage = obj.getJSONArray("listeMessage");
                Message message = new Message();
                user.setListMessage(message.readJsonList(listMessage));
            }
            catch (JSONException e){
                user.setListMessage(null);
            }
            list.add(user);
        }
        return list;
    }

    public void readJson(JSONObject json)throws JSONException{
        this.setId(json.getInt("id"));
        this.setPseudo(json.getString("pseudo"));
        this.setPassword(json.getString("motDePasse"));
        this.setSex(json.getString("sexe"));
        try{
            JSONArray array = json.getJSONArray("listeSujet");
            Sujet subject = new Sujet();
            this.setListSubject(subject.readJsonList(array));
        }
        catch (JSONException e){
            this.setListSubject(null);
        }

        try{
            JSONArray array = json.getJSONArray("listeMessage");
            Message message = new Message();
            this.setListMessage(message.readJsonList(array));
        }
        catch (JSONException e){
            this.setListMessage(null);
        }
    }

    @Override
    public String toString(){
        return ""+pseudo;
    }
}
