package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.pierard.projectforum.Async.DisplaySubjectAsync;
import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.Subject;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class DisplaySubjectActivity extends AppCompatActivity {

    // Data
    private User userCurrent = new User();
    private int cpt = 0;
    public List<User> list;

    // Affichage liste sujet
    public void populateListSubject(){
        List<String> array = new ArrayList<>();

        for (User u : list){
            for(Subject s : u.getListSujet()){
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

            User user = new User();
            Subject subject = new Subject();
            cpt = 0;
            // selected item
            for (User u : list){
                for(Subject s : u.getListSujet()){
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
            Global.generateToast(getResources().getString(R.string.toastErrorName), DisplaySubjectActivity.this);
        }
    };

    // Recu reponse DisplaySubjectAsync
    public void response(int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), DisplaySubjectActivity.this);
                break;
            case 201 :
                populateListSubject();
                break;
            case 204:
                TextView subTitle = (TextView) findViewById(R.id.textView6);
                subTitle.setVisibility(View.INVISIBLE);
                ListView scroll = findViewById(R.id.flowListSubject);
                scroll.setVisibility(View.INVISIBLE);
                Global.generateToast(getResources().getString(R.string.listSubjectEmptyName), DisplaySubjectActivity.this);
                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), DisplaySubjectActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), DisplaySubjectActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), DisplaySubjectActivity.this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_subject);

        // Récupération informations activitée précédente
        Intent intentRecup = getIntent();
        userCurrent = (User) intentRecup.getSerializableExtra("Object");

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