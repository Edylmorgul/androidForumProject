package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.pierard.projectforum.Async.LoginAsync;
import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class LoginActivity extends AppCompatActivity {

    // Data
    private EditText email;
    private EditText password;

    // Validation connexion
    private View.OnClickListener connectionListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            // Vérifier la validation des champs
            if(!checkData()){
                try{
                    new LoginAsync(LoginActivity.this).execute(email.getText().toString().toLowerCase(), password.getText().toString());
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), LoginActivity.this);
                }
            }
        }
    };

    // Vérifier les champs
    public boolean checkData(){
        // Données
        boolean error = false;

        // Récupération des champs
        email = findViewById(R.id.emailTextLogin);
        password = findViewById(R.id.passwordTextLogin);

        // Test des différents champs
        if(email.getText().toString().isEmpty()){
            email.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(password.getText().toString().isEmpty()){
            password.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        return error;
    };

    // Retour menu principal
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), LoginActivity.this);
        }
    };

    // Recu reponse LoginAsync
    public void response(User param, int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), LoginActivity.this);
                break;
            case 201 :
                try{
                    Intent intent = new Intent(LoginActivity.this, ActionUserActivity.class);
                    // Envoi objet utilisateur à l'activitée suivante
                    intent.putExtra("Object", param);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorConnectionName), LoginActivity.this);
                }
                break;
            case 204 :
                Global.generateToast(getResources().getString(R.string.toastErrorConnectionName), LoginActivity.this);
                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), LoginActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), LoginActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), LoginActivity.this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnConnection;
        btnConnection = (Button) findViewById(R.id.btnValidateLogin);
        btnConnection.setOnClickListener(connectionListener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackLogin);
        btnBack.setOnClickListener(backListener);
    }
}