package be.pierard.projectforum.Data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.pierard.projectforum.R;

// Classe utilitaire du projet
public class Global {
    private static String letterPattern = "[a-zA-Z]+$"; // N'autorise que les lettres minuscules/majuscules
    private static String numberPattern = "^[0-9]*$"; // N'autorise que les nombres;
    private static String emailPattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$"; // Vérifie la validité d'une adresse mail
    private static String phonePattern = "^[0-9]{3,4}[- .]?[0-9]{2}[- .]?[0-9]{2}[- .]?[0-9]{2}$"; // 10 nombres + permet les espaces, traits et points
    private static String passwordPattern = "^(?=.*[0-9])" // Doit contenir un chiffre
            + "(?=.*[a-z])(?=.*[A-Z])" // Doit contenir majuscule et minuscule
            + "(?=\\S+$).{4,20}$"; // Pas d'espace permis + longueur mdp 4 - 20

    public static String getLetterPattern() {
        return letterPattern;
    }
    public static String getNumberPattern() {
        return numberPattern;
    }
    public static String getEmailPattern() {
        return emailPattern;
    }
    public static String getPhonePattern() {
        return phonePattern;
    }
    public static String getPasswordPattern() {
        return passwordPattern;
    }

    public static Integer tryParseInt(String valeur) {
        try {
            return Integer.parseInt(valeur);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double tryParseDouble(String valeur) {
        try {
            return Double.parseDouble(valeur);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date tryParseDate(String valeur, SimpleDateFormat formatter) {
        try {
            return formatter.parse(valeur);
        }

        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Message pop-up
    public static void generateToast(String text, Activity context){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Pop-up exit
    public static void generatePopUpExit(Activity context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.exitTitlePopUpName);
        builder.setMessage(R.string.exitPopUpName);

        builder.setPositiveButton(R.string.yesName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){

                context.finish();

            }
        });

        builder.setNegativeButton(R.string.noName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){

                dialogInterface.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
