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

import be.pierard.projectforum.Activities.LoginActivity;
import be.pierard.projectforum.Data.Utilisateur;

public class LoginAsync extends AsyncTask<Utilisateur, Void, Utilisateur> {

    // Data
    private int code;
    private LoginActivity activity;

    // Constructor
    public LoginAsync(LoginActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Utilisateur doInBackground(Utilisateur... params) {
        String baseUrl = BaseUrl.URL;
        String rpc = "login.php";
        String uri = baseUrl + rpc;
        Utilisateur user = params[0];
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
            String postParams = "pseudo="+user.getPseudo()+"&motDePasse="+user.getPassword();
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
                    JSONObject utilisateur = json.getJSONObject("utilisateur");
                    user.readJson(utilisateur);
                }
            }
            return user;
        }
        catch(Exception e){
            code = 1;
            return user;
        }
    }

    protected void onPostExecute(Utilisateur params) {
        this.activity.response(params, code);
    }
}
