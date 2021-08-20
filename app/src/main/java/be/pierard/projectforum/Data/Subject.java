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
    public List<Subject> readJsonList(JSONArray array) throws JSONException{
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

    public Subject readJson(JSONObject json)throws JSONException {
        Subject subject = new Subject();
        subject.setId(json.getInt("id"));
        subject.setTopicTitle(json.getString("titreSujet"));
        subject.setDescription(json.getString("description"));

        try{
            JSONArray array = json.getJSONArray("listeMessage");
            Message message = new Message();
            this.setListMessage(message.readJsonList(array));
        }
        catch (JSONException e){
            this.setListMessage(null);
        }
        return subject;
    }
}
