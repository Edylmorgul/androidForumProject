package be.pierard.projectforum.Async;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import be.pierard.projectforum.Activities.CreateMessageActivity;
import be.pierard.projectforum.Data.Message;
import be.pierard.projectforum.Data.Subject;
import be.pierard.projectforum.Data.User;

public class CreateMessageAsync extends AsyncTask<Message, Void, Message> {

    // Data
    private int code;
    private Subject subject;
    private CreateMessageActivity activity;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    // Constructor
    public CreateMessageAsync(CreateMessageActivity activity, Subject subject){
        this.activity = activity;
        this.subject = subject;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Message doInBackground(Message... params) {
        String baseUrl = BaseUrl.URL;
        String rpc = "createMessage.php";
        String uri = baseUrl + rpc;
        Message message = params[0];
        String reponse ="";
        String dateString = sdf.format(params[0].getDateMessage());

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
            String postParams = "idUtilisateur="+message.getUser().getId()+"&idSujet="+subject.getId()+"&contenu="+message.getContent()+"&date="+dateString;
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
                    message.setId(json.getInt("id"));
                }
            }
            return message;
        }
        catch(Exception e){
            code = 1;
            return message;
        }
    }

    protected void onPostExecute(Message params) {
        this.activity.response(params, code);
    }
}
