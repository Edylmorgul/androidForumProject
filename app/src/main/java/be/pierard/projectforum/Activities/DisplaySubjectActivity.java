package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.pierard.projectforum.Async.DisplaySubjectAsync;
import be.pierard.projectforum.Data.Sujet;
import be.pierard.projectforum.Data.Utilisateur;
import be.pierard.projectforum.R;

public class DisplaySubjectActivity extends AppCompatActivity {

    // Data
    private Utilisateur userCurrent = new Utilisateur();
    private int cpt = 0;
    public List<Utilisateur> list;

    // Affichage liste sujet
    public void populateListSubject(){
        List<String> array = new ArrayList<>();

        for (Utilisateur u : list){
            for(Sujet s : u.getListSujet()){
                cpt ++;
                array.add(cpt + " " + getResources().getString(R.string.authorName) + " " + u.getPseudo() +" " + getResources().getString(R.string.subjectName) + " " + s.getTopicTitle());
            }
        }
        ListView scroll = findViewById(R.id.flowListSubject);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplaySubjectActivity.this, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scroll.setAdapter(adapter);

        // Obtenir élément listViews
        scroll.setOnItemClickListener((parent, view, position, id) -> {

            Utilisateur user = new Utilisateur();
            Sujet subject = new Sujet();
            cpt = 0;
            // selected item
            for (Utilisateur u : list){
                for(Sujet s : u.getListSujet()){
                    if(cpt == position){
                        user = u;
                        subject = s;
                    }
                    cpt++;
                }
            }

            Intent intent = new Intent(DisplaySubjectActivity.this, CreateMessageActivity.class);
            // Envoi objet utilisateur/sujet à l'activitée suivante
            intent.putExtra("ObjectUserCurrent", userCurrent);
            intent.putExtra("ObjectUser", user);
            intent.putExtra("ObjectSubject", subject);
            startActivity(intent);
            finish();
        });
    }

    // Retour menu utilisateur
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(DisplaySubjectActivity.this, ActionUserActivity.class);
            intent.putExtra("Object", userCurrent);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Message pop-up
    private void generateToast(String text){
        Toast toast = Toast.makeText(DisplaySubjectActivity.this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    // Recu reponse DisplaySubjectAsync
    public void response(int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                populateListSubject();
                break;
            case 204:
                TextView subTitle = (TextView) findViewById(R.id.textView6);
                subTitle.setVisibility(View.INVISIBLE);
                ListView scroll = findViewById(R.id.flowListSubject);
                scroll.setVisibility(View.INVISIBLE);
                generateToast(getResources().getString(R.string.listSubjectEmptyName));
                break;
            case 401 :
                generateToast(getResources().getString(R.string.toastErrorSqlLiteName));
                break;
            case 403 :
                generateToast(getResources().getString(R.string.toastErrorBddNotFoundName));
                break;
            case 500 :
                generateToast(getResources().getString(R.string.toastErrorServerName));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_subject);

        // Récupération informations activitée précédente
        Intent intentRecup = getIntent();
        userCurrent = (Utilisateur) intentRecup.getSerializableExtra("Object");

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackDisplaySubject);
        btnBack.setOnClickListener(backListener);

        // Affichage liste des sujets
        new DisplaySubjectAsync(DisplaySubjectActivity.this).execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Afin d'éviter que compteur s'incrémente à chaque mise en veille du gsm
        cpt = 0;
        // Affichage liste des messages du sujet
        new DisplaySubjectAsync(DisplaySubjectActivity.this).execute();
    }
}