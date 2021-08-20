package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.pierard.projectforum.Async.LoginAsync;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class LoginActivity extends AppCompatActivity {

    // Data
    private User user;

    // Validation connexion
    private View.OnClickListener connectionListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            EditText txt;
            String email;
            String password;

            // Récupération des champs
            txt = findViewById(R.id.emailTextLogin);
            email = txt.getText().toString();
            txt = findViewById(R.id.passwordTextLogin);
            password = txt.getText().toString();
            try{
                // Test champs vides
                if(email.isEmpty() || password.isEmpty()){
                    generateToast(getResources().getString(R.string.toastErrorFormName));
                }

                // Creation objet
                else{
                    new LoginAsync(LoginActivity.this).execute(email, password);
                }
            }
            catch (Exception e){
                generateToast(getResources().getString(R.string.toastErrorName));
            }
        }
    };

    // Retour menu principal
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Recu reponse LoginAsync
    public void response(User param, int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
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
                    generateToast(getResources().getString(R.string.toastErrorConnectionName));
                }
                break;
            case 204 :
                generateToast(getResources().getString(R.string.toastErrorConnectionName));
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
        Toast toast = Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
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