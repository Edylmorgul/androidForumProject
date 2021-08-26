package be.pierard.projectforum.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable, Comparable<Message> {

    // Data
    private int id;
    private String content;
    private Date dateMessage;
    private User user;

    // Constructor
    public Message(){

    }

    public Message(String content, Date dateMessage, User user){
        this.content = content;
        this.dateMessage = dateMessage;
        this.user = user;
    }

    // GET/SET
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Date getDateMessage(){
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

    // Methods
    public static List<Message> getJsonList(JSONArray array) throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate;
        Date date = null;
        List<Message> list = new LinkedList<Message>();
        for(int i=0 ; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Message message = new Message();
            User user = new User();
            user.setPseudo(obj.getString("pseudo"));
            user.setPassword(obj.getString("motDePasse"));
            user.setSex(obj.getString("sexe"));
            message.setId(obj.getInt("idMessage"));
            message.setContent(obj.getString("contenu"));
            strDate = obj.getString("dateMessage");
            date = Global.tryParseDate(strDate, format);
            message.setDateMessage(date);
            list.add(message);
            message.setUser(user);
        }
        return list;
    }

    public void getJson(JSONObject json)throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate;
        Date date = null;

        this.setId(json.getInt("idMessage"));
        this.setContent(json.getString("message"));
        strDate = json.getString("dateMessage");
        date = Global.tryParseDate(strDate, format);
        this.setDateMessage(date);
    }

    // Pour comparaison des dates(Tri)
    @Override
    public int compareTo(Message o) {
        if(getDateMessage() == null || o.getDateMessage() == null){
            return 0;
        }
        return getDateMessage().compareTo(o.dateMessage);
    }

    @Override
    public String toString(){
        return ""+content + dateMessage;
    }
}
