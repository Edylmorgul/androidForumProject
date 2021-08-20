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
import java.util.Scanner;

import be.pierard.projectforum.Activities.CreateSubjectActivity;
import be.pierard.projectforum.Data.Subject;
import be.pierard.projectforum.Data.User;

public class CreateSubjectAsync extends AsyncTask<Subject, Void, Subject> {

    // Data
    private int code;
    private User user;
    private CreateSubjectActivity activity;

    // Constructor
    public CreateSubjectAsync(CreateSubjectActivity activity, User user){
        this.activity = activity;
        this.user = user;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Subject doInBackground(Subject... params) {
        String baseUrl = BaseUrl.URL;
        String rpc = "createSubject.php";
        String uri = baseUrl + rpc;
        Subject subject = params[0];
        String reponse ="";

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
            String postParams = "titre="+subject.getTopicTitle()+"&idUtilisateur="+user.getId()+"&description="+subject.getDescription();
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
                    subject.setId(json.getInt("id"));
                }
            }
            return subject;
        }
        catch(Exception e){
            code = 1;
            return subject;
        }
    }

    protected void onPostExecute(Subject params) {
        this.activity.response(params, code);
    }
}
