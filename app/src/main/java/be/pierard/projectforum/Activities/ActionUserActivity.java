package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.pierard.projectforum.Data.Utilisateur;
import be.pierard.projectforum.R;

public class ActionUserActivity extends AppCompatActivity {

    // Data
    private Utilisateur user = new Utilisateur();

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
            generateToast(getResources().getString(R.string.toastErrorName));
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
            generateToast(getResources().getString(R.string.toastErrorName));
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
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Message pop-up
    private void generateToast(String text){
        Toast toast = Toast.makeText(ActionUserActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_user);

        // Récupération informations activitée précédente
        Intent intentRecup = getIntent();
        user = (Utilisateur) intentRecup.getSerializableExtra("Object");
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