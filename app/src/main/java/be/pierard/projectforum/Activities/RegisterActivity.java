package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import be.pierard.projectforum.Async.CreateUserAsync;
import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.Data.City;
import be.pierard.projectforum.R;

public class RegisterActivity extends AppCompatActivity {

    // Data
    private User user;
    private City city;
    private String textGender;
    private EditText pseudo;
    private EditText email;
    private EditText password;
    private EditText passwordVerif;
    private EditText nameCity;
    private EditText strCp;
    public List<City> list;

    // Valider inscription
    private View.OnClickListener registerListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // Données
            CheckBox conditionsCheckBox = (CheckBox) findViewById(R.id.conditions);

            // Vérifier si condition acceptée
            if(conditionsCheckBox.isChecked()){
                // Vérifier la validation des champs
                if(!checkData()){
                    int cp = Global.tryParseInt(strCp.getText().toString());
                    try{
                        city = new City(nameCity.getText().toString(), cp);
                        user = new User(pseudo.getText().toString(), password.getText().toString(), textGender, email.getText().toString().toLowerCase(), city);
                        // Tâche asynchrone instancié dans le bouton ==> Execute va exécuter les méthodes du RegisterAsync
                        new CreateUserAsync(RegisterActivity.this).execute(user);
                    }
                    catch (Exception e){
                        Global.generateToast(getResources().getString(R.string.toastErrorName), RegisterActivity.this);
                    }
                }
            }
            else
                Global.generateToast(getResources().getString(R.string.errorConditionsName), RegisterActivity.this);
        }
    };

    // Vérifier les champs
    public boolean checkData(){
        // Données
        boolean error = false;

        // Récupération des champs
        pseudo = findViewById(R.id.pseudoTextRegister);
        email = findViewById(R.id.emailTextRegister);
        password = findViewById(R.id.passwordTextRegister);
        passwordVerif = findViewById(R.id.passwordConfirmTextRegister);
        nameCity = findViewById(R.id.cityTextRegister);
        strCp = findViewById(R.id.postalCodeTextRegister);

        // Test des différents champs
        if(pseudo.getText().toString().isEmpty()){
            pseudo.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(pseudo.getText().toString().length() < 4){
            pseudo.setError(getResources().getString(R.string.toastErrorCharacterNumberName));
            error = true;
        }

        if(email.getText().toString().isEmpty()){
            email.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(!email.getText().toString().matches(Global.getEmailPattern())){
            email.setError(getResources().getString(R.string.emailRegexName));
            error = true;
        }

        if(password.getText().toString().isEmpty()){
            password.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(!password.getText().toString().matches(Global.getPasswordPattern())){
            password.setError(getResources().getString(R.string.toastErrorPasswordRegexName));
            error = true;
        }

        if(passwordVerif.getText().toString().isEmpty()){
            passwordVerif.setError(getResources().getString(R.string.errorForm));
            error =  true;
        }

        if(!password.getText().toString().equals(passwordVerif.getText().toString())){
            Global.generateToast(getResources().getString(R.string.toastErrorPasswordName), RegisterActivity.this);
            error =  true;
        }

        if(nameCity.getText().toString().isEmpty()){
            nameCity.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(!nameCity.getText().toString().matches(Global.getLetterPattern())){
            nameCity.setError(getResources().getString(R.string.toastErrorRegexName));
            error = true;
        }

        if(strCp.getText().toString().isEmpty()){
            strCp.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        if(Global.tryParseInt(strCp.getText().toString()) == null){
            strCp.setError(getResources().getString(R.string.errorPostalCode));
            error = true;
        }

        return error;
    }

    // Récupérer ville
    public void populateSpinnerCity(){
        Spinner spinner = findViewById(R.id.spinCity);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            EditText txtNameCity;
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                // Obtenir élément spinner
                String textCity = item.toString();
                // Compléter champs texte ville
                txtNameCity = findViewById(R.id.cityTextRegister);
                txtNameCity.setText(textCity);
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Récupérer genre
    public void populateSpinnerGender(){
        Spinner spinner = findViewById(R.id.spinGender);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                // Obtenir élément spinner
                textGender = item.toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Retour menu principal
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            Global.generateToast(getResources().getString(R.string.toastErrorName), RegisterActivity.this);
        }
    };

    // Recu reponse RegisterAsync
    public void response(User param, int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), RegisterActivity.this);
                break;
            case 201 :
                try{
                    Global.generateToast(getResources().getString(R.string.toastValidateName), RegisterActivity.this);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), RegisterActivity.this);
                }
                break;
            case 221 :
                Global.generateToast(getResources().getString(R.string.toastErrorPseudoExistName), RegisterActivity.this);
                break;
            case 222 :
                Global.generateToast(getResources().getString(R.string.toastErrorEmailExistName), RegisterActivity.this);
                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), RegisterActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), RegisterActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), RegisterActivity.this);
                break;
        }
    }

    // Recu reponse DisplayCityAsync
    public void response(int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), RegisterActivity.this);
                break;
            case 201 :

                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), RegisterActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), RegisterActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), RegisterActivity.this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnValidate;
        btnValidate = (Button) findViewById(R.id.btnValidateRegister);
        btnValidate.setOnClickListener(registerListener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackRegister);
        btnBack.setOnClickListener(backListener);

        // Chargement de certains éléments à la création de l'activité
        populateSpinnerCity();
        populateSpinnerGender();
    }
}