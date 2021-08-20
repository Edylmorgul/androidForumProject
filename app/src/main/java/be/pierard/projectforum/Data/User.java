package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    // Data
    protected int id;
    protected String pseudo;
    protected String password;
    protected String sex;
    protected String email;
    protected int active = 1;
    protected List<Subject> listSubject;
    protected List<Message> listMessage;

    // Constructor
    public User(){

    }

    public User(String pseudo, String password, String sex, String email, int active){
        this.pseudo = pseudo;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.active = active;
        listSubject = new LinkedList<Subject>();
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

    public String getEmail(){return email; }

    public void setEmail(String email) {this.email = email; }

    public int getActive(){ return active; }

    public void setActive(int active) {this.active = active; }

    public List<Subject> getListSujet(){
        return listSubject;
    }

    public void setListSubject(List<Subject> listSubject){
        this.listSubject = listSubject;
    }

    public List<Message> getListMessage(){
        return listMessage;
    }

    public void setListMessage(List<Message> listMessage){
        this.listMessage = listMessage;
    }

    // Methods
    public List<User> readJsonList(JSONArray array) throws JSONException{
        List<User> list = new LinkedList<User>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            User user = new User();
            user.setId(obj.getInt("id"));
            user.setPseudo(obj.getString("pseudo"));
            user.setPassword(obj.getString("motDePasse"));
            user.setSex(obj.getString("sexe"));
            user.setEmail(obj.getString("email"));
            user.setActive(obj.getInt("actif"));
            try{
                JSONArray listSubject = obj.getJSONArray("listeSujet");
                Subject subject = new Subject();
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
        this.setEmail(json.getString("email"));
        this.setActive(json.getInt("actif"));
        try{
            JSONArray array = json.getJSONArray("listeSujet");
            Subject subject = new Subject();
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
