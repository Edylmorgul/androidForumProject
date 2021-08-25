package be.pierard.projectforum.Async;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import be.pierard.projectforum.Activities.RegisterActivity;
import be.pierard.projectforum.Data.City;

public class DisplayCityAsync extends AsyncTask<Void, Void, List<City>> {

    // Data
    private int code;
    private RegisterActivity activity;

    // Constructor
    public DisplayCityAsync(RegisterActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<City> doInBackground(Void... params) {
        String uriBase = BaseUrl.URL;
        String rpc = "getAllCity.php";
        String uri = uriBase +rpc;
        String reponse ="";
        List<City> list = new LinkedList<>();
        City city = new City();
        try{
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(15000);
            connection.connect();

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
                    JSONArray jsonArray = json.getJSONArray("listeVille");
                    list = city.getJsonList(jsonArray);
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
    protected void onPostExecute(List<City> params) {
        this.activity.list = params;
        this.activity.response(code);
    }
}
