package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Subject implements Serializable {

    // Data
    private int id;
    private String topicTitle;
    private String description;
    private List<Message> listMessage;

    // Contructor
    public Subject(){

    }

    public Subject(String topicTitle, String description){
        this.topicTitle = topicTitle;
        this.description = description;
        listMessage = new LinkedList<Message>();
    }

    // GET/SET
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTopicTitle(){
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle){
        this.topicTitle = topicTitle;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public List<Message> getListMessage(){
        return listMessage;
    }

    public void setListMessage(List<Message> listMessage){
        this.listMessage = listMessage;
    }

    // Methods
    public static List<Subject> getJsonList(JSONArray array) throws JSONException{
        List<Subject> list = new LinkedList<Subject>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Subject subject = new Subject();
            subject.setId(obj.getInt("id"));
            subject.setTopicTitle(obj.getString("titreSujet"));
            subject.setDescription(obj.getString("description"));
            list.add(subject);
        }
        return list;
    }

    public void getJson(JSONObject json)throws JSONException {
        this.setId(json.getInt("id"));
        this.setTopicTitle(json.getString("titreSujet"));
        this.setDescription(json.getString("description"));

        try{
            JSONArray array = json.getJSONArray("listeMessage");
            this.setListMessage(Message.getJsonList(array));
        }
        catch (JSONException e){
            this.setListMessage(null);
        }
    }
}
