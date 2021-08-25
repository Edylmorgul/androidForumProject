package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class ActionUserActivity extends AppCompatActivity {

    // Data
    private User user = new User();

    // Vers creation d'un sujet
    private View.OnClickListener createSubjectListener = v -> {
        try{
            Intent intent = new Intent(ActionUserActivity.this, CreateSubjectActivity.class);
            // Envoi objet utilisateur à l'activitée suivante
            intent.putExtra("Object", user);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), ActionUserActivity.this);
        }
    };

    // Vers liste sujets
    private View.OnClickListener displaySubjectListener = v -> {
        try{
            Intent intent = new Intent(ActionUserActivity.this, DisplaySubjectActivity.class);
            // Envoi objet utilisateur à l'activitée suivante
            intent.putExtra("Object", user);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), ActionUserActivity.this);
        }
    };

    // Deconnexion compte
    private View.OnClickListener disconnectListener = v -> {
        try{
            Intent intent = new Intent(ActionUserActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), ActionUserActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_user);

        // Récupération informations activitée précédente
        Intent intentRecup = getIntent();
        user = (User) intentRecup.getSerializableExtra("Object");
        TextView txtName = findViewById(R.id.txtNameUser);
        txtName.setText(user.getPseudo());

        Button btnCreateSubject;
        btnCreateSubject = (Button) findViewById(R.id.btnCreateSubject);
        btnCreateSubject.setOnClickListener(createSubjectListener);

        Button btnDisplaySubject;
        btnDisplaySubject = (Button) findViewById(R.id.btnListSubject);
        btnDisplaySubject.setOnClickListener(displaySubjectListener);

        Button btnDisco;
        btnDisco = (Button) findViewById(R.id.btnDisconnectActionUser);
        btnDisco.setOnClickListener(disconnectListener);
    }
}