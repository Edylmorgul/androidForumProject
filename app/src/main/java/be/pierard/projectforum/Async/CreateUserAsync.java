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

import be.pierard.projectforum.Activities.RegisterActivity;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.Data.City;

public class CreateUserAsync extends AsyncTask<User, Void, User> { //Type donnée entrée pour appel X, Type unité de progression(Souvent entier) Y, Type résultat de retour appel Z

    // Data
    private RegisterActivity activity;
    private int code;

    // Constructor
    public CreateUserAsync(RegisterActivity activity){
        this.activity = activity;
    }

    // prétraitement de l'appel
    @Override
    protected void onPreExecute() { // S'allie au 2e type asyncTask ==> Utile pour les bars de progressions

    }

    // Appel de procédure distante via HTTP (traitement long) ==> Toutes opérations qui prend du temps à l'exécution
    // Utilisateur... ==> Un nombre indéfini d'utilisateur reçus
    @Override
    protected User doInBackground(User... params) { // Donnée paramètre methode  s'allie au 1er type de données asyncTask X
        String baseUrl = BaseUrl.URL;
        String rpc = "createUser.php";
        String uri = baseUrl + rpc;
        User user = params[0]; // 1er élément transmis(comme tableau)
        String reponse = "";

        try{
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(15000);
            connection.connect(); // Va essayer de se connecter au RPC php

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String postParams = "pseudo="+user.getPseudo()+"&email="+user.getEmail()+"&motDePasse="+user.getPassword()+"&sexe="+user.getSex()+"&actif="+user.getActive()+"&ville="+user.getCity().getNameCity()+"&cp="+user.getCity().getCp();
            writer.write(postParams);
            writer.flush();
            writer.close(); // On ferme le buffer
            os.close(); // On ferme le outPutStream
            int responseCode = connection.getResponseCode(); // Récupération du code réponse du serveur

            // Si connexion serveur ok
            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                Scanner scanner = new Scanner(inputStreamReader); // Va lire, ligne par ligne, mot par mot le contenu du RPC
                scanner.useDelimiter("\n"); // Parcourir fichier saut de ligne = next
                while (scanner.hasNext()) { // Va lire chaque chaine de caractère, chaines séparées par des espaces, un scanner lit jusqu'à un espace
                    reponse += scanner.next(); // reponse prend la concaténation des chaines
                }
                JSONObject json = new JSONObject(reponse); // Récupérer JSON dans php via les noms
                code = json.getInt("code"); // Réception valeur code
                if(code == 201){
                    user.setId(json.getInt("id")); // Récupération de l'id utilisateur
                }
            }
            return user; //Objet de type Z automatiquement réinjecté en entrée de onPostExecute()
        }
        catch(Exception e){
            code = 1;
            return user;
        }
    }
    // post-traitement de l'appel
    // Envoi de reponse post ==> Code
    protected void onPostExecute(User params) { // S'allie au 3e type de données asyncTask Z
            this.activity.response(params, code);
    }
}

/*onPreExecute()  est appelée et permet de préparer la tâche, par exemple l’interface graphique (animation d’attente, notification).
W doInBackground(U)  est ensuite appelée et s’exécute dans un thread secondaire. Elle reçoit le paramètre d’input de la tâche et à la fin, retourne un résultat de type W.
onPostExecute(W)  est enfin appelée, et s’exécute dans le thread principal : elle permet d’afficher le résultat par exemple.
onProgressUpdate(V...)  permet de gérer les notifications de progression de la tâche.*/
