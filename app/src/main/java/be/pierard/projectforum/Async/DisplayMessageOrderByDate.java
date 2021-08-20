package be.pierard.projectforum.Async;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import be.pierard.projectforum.Activities.CreateMessageActivity;
import be.pierard.projectforum.Data.Message;
import be.pierard.projectforum.Data.Subject;

public class DisplayMessageOrderByDate extends AsyncTask<Subject, Void, List<Message>> {
    // Data
    private int code;
    private CreateMessageActivity activity;

    // Constructor
    public DisplayMessageOrderByDate(CreateMessageActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<Message> doInBackground(Subject... params) {
        String uriBase = BaseUrl.URL;
        String rpc = "orderByMessageByDate.php";
        String uri = uriBase +rpc;
        String reponse ="";
        Subject subject = params[0];
        List<Message> list = new LinkedList<>();
        Message message = new Message();
        try{
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(15000);
            connection.connect();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String postParams = "idSujet="+subject.getId();
            writer.write(postParams);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                Scanner scanner = new Scanner(inputStreamReader);
                scanner.useDelimiter("\n");
                while (scanner.hasNext()) {
                    reponse += scanner.next();
                }
                JSONObject json = new JSONObject(reponse);
                code = json.getInt("code");
                if(code == 201){
                    JSONArray jsonArray = json.getJSONArray("listeMessageTri");
                    list = message.readJsonList(jsonArray);
                    return list;
                }
                return list;
            }
        }
        catch (Exception e){
            code = 1;
            return list;
        }
        return list;
    }

    protected void onPostExecute(List<Message> params) {
        this.activity.list = params;
        this.activity.response(code);
    }
}
