package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.R;

public class MainActivity extends AppCompatActivity {

    // Data

    // Connxion utilisateur
    private View.OnClickListener loginListener = v -> { // Lambda expression ==> Tips android
        try{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), MainActivity.this);
        }
    };

    // Inscription utilisateur
    private View.OnClickListener registerListener = v -> {
        try{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), MainActivity.this);
        }
    };

    // Quitter le programme
    private View.OnClickListener exitListener = v ->{
        Global.generatePopUpExit(MainActivity.this);
    };

    // Cycle de vie d'une application android
    // Permet de concevoir l'interface graphique et crée l'activité ==> Se lance automatiquement (Obligatoire)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Crée l'activité

        Button btnLogin;
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(loginListener);

        Button btnRegister;
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(registerListener);

        Button btnExit;
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(exitListener);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }*/
}

// La mise en veille d'une activité ne détruit pas l'activité, elle passe par onRestart ==> onStart ==> onResume
// onPause si activité s'intercale devant comme par exemple un appel téléphonique ou alors une redirection vers une autre activité
// onStop ==> Mise en veille activité
// Le changement d'orientation du gsm détruit l'activité et ensuite est récréée ==> Perte donc de donnée en cas de rotation du gsm