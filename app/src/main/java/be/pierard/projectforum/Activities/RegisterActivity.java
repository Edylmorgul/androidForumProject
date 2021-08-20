package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import be.pierard.projectforum.Async.RegisterAsync;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.Data.City;
import be.pierard.projectforum.R;

public class RegisterActivity extends AppCompatActivity {

    // Data
    private User user;
    private City city;
    public List<City> list;

    // Valider inscription
    private View.OnClickListener registerListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            RadioGroup rg;
            RadioButton radioButton;
            EditText txtPseudo;
            EditText txtPassword;
            EditText txtConfirmPassword;
            EditText txtNameCity;
            String pseudo;
            String password;
            String passwordVerif;
            String nameCity;
            String sex;
            String passwordPattern = "^(?=.*[0-9])" // Doit contenir un chiffre
                                    + "(?=.*[a-z])(?=.*[A-Z])" // Doit contenir majuscule et minuscule
                                    + "(?=\\S+$).{4,20}$"; // Pas d'espace permis + longueur mdp 4 - 20
            String cityPattern = "[a-zA-Z]+"; // Ne doit contenir que des lettres

            // Récupération des champs
            txtPseudo = findViewById(R.id.pseudoTextRegister);
            pseudo = txtPseudo.getText().toString();
            txtPassword = findViewById(R.id.passwordTextRegister);
            password = txtPassword.getText().toString();
            txtConfirmPassword = findViewById(R.id.passwordConfirmTextRegister);
            passwordVerif = txtConfirmPassword.getText().toString();
            txtNameCity = findViewById(R.id.cityTextRegister);
            nameCity = txtNameCity.getText().toString();
            // Récupération du sexe
            rg = (RadioGroup) findViewById(R.id.radioGroup);
            // Récupération du bouton selectionné depuis le radioGroup
            int selectedId = rg.getCheckedRadioButtonId();
            // Récupération de la valeur du bouton selectionné
            radioButton = (RadioButton) findViewById(selectedId);
            sex = (String) radioButton.getText();

            // Réinitialiser les codes couleurs
            errorColor(0, txtPseudo);
            errorColor(0, txtPassword);
            errorColor(0, txtConfirmPassword);
            errorColor(0, txtNameCity);

            try{
                // Test champs vides
                if(pseudo == null || pseudo.equals("") || password == null || password.equals("") || passwordVerif == null || passwordVerif.equals("") || nameCity == null || nameCity.equals("")){
                    generateToast(getResources().getString(R.string.toastErrorFormName));
                }

                // Test nombre de caractères ==> Il existe une ville avec un seul caractère
                else if(pseudo.length() < 4){
                    generateToast(getResources().getString(R.string.toastErrorCharacterNumberName));
                    errorColor(1, txtPseudo);
                }

                // Test regex mot de passe
                else if(password.matches(passwordPattern) == false){
                    generateToast(getResources().getString(R.string.toastErrorPasswordRegexName));
                    errorColor(1, txtPassword);
                }

                // Test mot de passe identique
                else if(!password.equals(passwordVerif)){
                    generateToast(getResources().getString(R.string.toastErrorPasswordName));
                    errorColor(1, txtPassword);
                    errorColor(1, txtConfirmPassword);
                }

                // Test regex nom ville
                else if(nameCity.matches(cityPattern) == false){
                    generateToast(getResources().getString(R.string.toastErrorRegexName));
                    errorColor(1, txtNameCity);
                }

                // Création objets
                else{
                    //city = new City(nameCity);
                    //user = new User(pseudo,password,sex);
                    // Tâche asynchrone instancié dans le bouton ==> Execute va exécuter les méthodes du RegisterAsync
                    new RegisterAsync(RegisterActivity.this,city).execute(user);
                }
            }
            catch (Exception e){
                generateToast(getResources().getString(R.string.toastErrorName));
            }
        }
    };

    // Affichage ville ==> Trouver sur internet
    public void populateSpinnerCity(){
        /*List<String> arraySpinner = new ArrayList<>();

        for(Ville v : list){
            arraySpinner.add(v.getNameCity());
        }

        Spinner spinner = findViewById(R.id.spinCity);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);*/

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

    // Retour menu principal
    private View.OnClickListener backListener = v -> {
        try{
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Recu reponse RegisterAsync
    public void response(User param, int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                try{
                    generateToast(getResources().getString(R.string.toastValidateName));
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    generateToast(getResources().getString(R.string.toastErrorName));
                }
                break;
            case 218 :
                generateToast(getResources().getString(R.string.toastErrorPseudoExistName));
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

    // Recu reponse DisplayVilleAsync
    public void response(int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                //populateSpinnerCity();
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
        Toast toast = Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    // Code couleur pour signifier erreur(Petit bug avec la couleur verte qui rest ==> à corriger)
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
        setContentView(R.layout.activity_register);

        Button btnValidate;
        btnValidate = (Button) findViewById(R.id.btnValidateRegister);
        btnValidate.setOnClickListener(registerListener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackRegister);
        btnBack.setOnClickListener(backListener);

        populateSpinnerCity(); // Si temps possible ==> Récup les villes en DB et gérer la saisie et la selection ==> Si pas, méthode temporaire avec liste en brut des villes
        //new DisplayCityAsync(RegisterActivity.this).execute();
    }
}