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
import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.Subject;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class CreateSubjectActivity extends AppCompatActivity {

    // Data
    private User user;
    private Subject subject;
    EditText txtTitle;
    EditText txtContent;

    // Création du sujet
    private View.OnClickListener createSubjectLitener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if(!checkData()){
                try{
                    // Creation objet
                    subject = new Subject(txtTitle.getText().toString(),txtContent.getText().toString());
                    new CreateSubjectAsync(CreateSubjectActivity.this, user).execute(subject);
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), CreateSubjectActivity.this);
                }
            }
        }
    };

    // Vérifier les champs
    public boolean checkData(){
        // Données
        boolean error = false;

        // Récupération des champs
        txtTitle = findViewById(R.id.titleTextSubject);
        txtContent = findViewById(R.id.messageTextSubject);

        // Test des différents champs
        if(txtTitle.getText().toString().isEmpty()){
            txtTitle.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(txtTitle.getText().toString().length() < 4){
            txtTitle.setError(getResources().getString(R.string.toastErrorCharacterNumberName));
            error = true;
        }

        if(txtContent.getText().toString().isEmpty()){
            txtContent.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        return error;
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
            Global.generateToast(getResources().getString(R.string.toastErrorName), CreateSubjectActivity.this);
        }
    };

    // Recu reponse CreateSubjectAsync
    public void response(Subject param, int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), CreateSubjectActivity.this);
                break;
            case 201 :
                try{
                    Global.generateToast(getResources().getString(R.string.toastValidateName), CreateSubjectActivity.this);
                    Intent intent = new Intent(CreateSubjectActivity.this, ActionUserActivity.class);
                    intent.putExtra("Object", user);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), CreateSubjectActivity.this);
                }
                break;
            case 218 :
                Global.generateToast(getResources().getString(R.string.toastErrorTitleExistName), CreateSubjectActivity.this);
                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), CreateSubjectActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), CreateSubjectActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), CreateSubjectActivity.this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);

        // Récupération informations activitée précédente
        user = new User();
        Intent intentRecup = getIntent();
        user = (User) intentRecup.getSerializableExtra("Object");

        Button btnValidate;
        btnValidate = (Button) findViewById(R.id.btnValidateSubject);
        btnValidate.setOnClickListener(createSubjectLitener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackSubject);
        btnBack.setOnClickListener(backListener);
    }
}