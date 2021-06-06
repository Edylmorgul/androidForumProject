package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.pierard.projectforum.Async.CreateSubjectAsync;
import be.pierard.projectforum.Data.Sujet;
import be.pierard.projectforum.Data.Utilisateur;
import be.pierard.projectforum.R;

public class CreateSubjectActivity extends AppCompatActivity {

    // Data
    private Utilisateur user = new Utilisateur();
    private Sujet subject;

    // Création du sujet
    private View.OnClickListener createSubjectLitener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            EditText txtTitle;
            EditText txtContent;
            String title;
            String content;

            // Récupération des champs
            txtTitle = findViewById(R.id.titleTextSubject);
            title = txtTitle.getText().toString();
            txtContent = findViewById(R.id.messageTextSubject);
            content = txtContent.getText().toString();

            // Réinitialiser les codes couleurs
            errorColor(0, txtTitle);
            errorColor(0, txtContent);

            try{
                // Test champs vide
                if(title == null || title.equals("") || content == null || content.equals("")){
                    generateToast(getResources().getString(R.string.toastErrorFormName));
                }

                // Test nombre de caractères
                else if(title.length() < 4){
                    generateToast(getResources().getString(R.string.toastErrorCharacterNumberName));
                    errorColor(1, txtTitle);
                }

                // Creation objet
                else{
                    subject = new Sujet(title,content);
                    new CreateSubjectAsync(CreateSubjectActivity.this, user).execute(subject);
                }
            }
            catch (Exception e){
                generateToast(getResources().getString(R.string.toastErrorName));
            }
        }
    };

    // Retour menu utilisateur
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(CreateSubjectActivity.this, ActionUserActivity.class);
            intent.putExtra("Object", user);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Recu reponse CreateSubjectAsync
    public void response(Sujet param, int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                try{
                    generateToast(getResources().getString(R.string.toastValidateName));
                    Intent intent = new Intent(CreateSubjectActivity.this, ActionUserActivity.class);
                    intent.putExtra("Object", user);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    generateToast(getResources().getString(R.string.toastErrorName));
                }
                break;
            case 218 :
                generateToast(getResources().getString(R.string.toastErrorTitleExistName));
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

    // Message pop-up
    private void generateToast(String text){
        Toast toast = Toast.makeText(CreateSubjectActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Code couleur pour signifier erreur
    private void errorColor(int flag, EditText txt){
        if(flag == 1){
            txt.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }

        else{
            txt.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);

        // Récupération informations activitée précédente
        Intent intentRecup = getIntent();
        user = (Utilisateur) intentRecup.getSerializableExtra("Object");

        Button btnValidate;
        btnValidate = (Button) findViewById(R.id.btnValidateSubject);
        btnValidate.setOnClickListener(createSubjectLitener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackSubject);
        btnBack.setOnClickListener(backListener);
    }
}