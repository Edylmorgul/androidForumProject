package be.pierard.projectforum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import be.pierard.projectforum.Async.CreateMessageAsync;
import be.pierard.projectforum.Async.DisplayMessageOrderByDate;
import be.pierard.projectforum.Data.Message;
import be.pierard.projectforum.Data.Sujet;
import be.pierard.projectforum.Data.Utilisateur;
import be.pierard.projectforum.R;

public class CreateMessageActivity extends AppCompatActivity {

    // Data
    private Utilisateur userCurrent = new Utilisateur();
    private Utilisateur user = new Utilisateur();
    private Sujet subject = new Sujet();
    private Message message;
    private LinearLayout layout;
    public List<Message> list;

    // Affichage liste messages
    public void populateListMessage() {
        layout.removeAllViews();
        int i = 0;
        // Tri liste
        for(Message m : list){
            TextView textView = new TextView(this);
            if(m.getUser().getSex().equals("Homme") || m.getUser().getSex().equals("Men") ){
                textView.setBackgroundColor(getResources().getColor(R.color.colorMen));
            }
            else{
                textView.setBackgroundColor(getResources().getColor(R.color.colorWomen));
            }
            textView.setTextSize(15);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setText(getResources().getString(R.string.authorName) + " " + m.getUser().getPseudo() + " : " + m.getContent() + " " + " DATE : " + m.getDateMessage());
            layout.addView(textView);
            textView.setId(i);
            i++;
        }
    }

    // Creéation objet message
    private View.OnClickListener validateListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            EditText textMessage;
            String content;

            textMessage = findViewById(R.id.messageTextSubjectByUser);
            content = textMessage.getText().toString();
            try{
                // Test champs vide
                if(content == null || content.equals("")){
                    generateToast(getResources().getString(R.string.toastErrorFormName));
                }

                // Creation objet message
                else{
                    // Récup date actuelle message
                    Date currentTime = new Date();
                    message = new Message(content,currentTime);
                    new CreateMessageAsync(CreateMessageActivity.this, userCurrent, subject).execute(message);
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
            Intent intent = new Intent(CreateMessageActivity.this, ActionUserActivity.class);
            intent.putExtra("Object", userCurrent);
            startActivity(intent);
            finish();
        }
        catch (Exception e){
            generateToast(getResources().getString(R.string.toastErrorName));
        }
    };

    // Recu reponse CreateMessageAsync
    public void response(Message param, int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                try{
                    generateToast(getResources().getString(R.string.toastValidateName));
                    // Refresh de la liste des messages
                    new DisplayMessageOrderByDate(CreateMessageActivity.this).execute(subject);
                    populateListMessage();
                    EditText textMessage = findViewById(R.id.messageTextSubjectByUser);
                    // Vide textView
                    textMessage.setText("");
                }
                catch (Exception e){
                    generateToast(getResources().getString(R.string.toastErrorName));
                }
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
        Toast toast = Toast.makeText(CreateMessageActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Recu reponse DisplayMessageBySubjectAsync
    @SuppressLint("ResourceType")
    public void response(int code) {
        switch (code){
            case 1 :
                generateToast(getResources().getString(R.string.toastErrorName));
                break;
            case 201 :
                populateListMessage();
                break;
            case 204:
                TextView textView = new TextView(this);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.setText(getResources().getString(R.string.messageEmptyName));
                layout.addView(textView);
                textView.setId(1);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        // Récupération informations activitée précédente
        Intent intentRecupUserCurrent = getIntent();
        userCurrent = (Utilisateur) intentRecupUserCurrent.getSerializableExtra("ObjectUserCurrent");
        Intent intentRecupUser = getIntent();
        user = (Utilisateur) intentRecupUser.getSerializableExtra("ObjectUser");
        Intent intentRecupSubject = getIntent();
        subject = (Sujet) intentRecupSubject.getSerializableExtra("ObjectSubject");

        // AAffichage information sujet pour message
        TextView text = findViewById(R.id.titleSubjectMessage);
        text.setText(subject.getTopicTitle());
        TextView descText = findViewById(R.id.messageTextSubjectMessage);
        descText.setText(subject.getDescription());

        layout = (LinearLayout) findViewById(R.id.linearLayoutMessage);

        Button btnValidate;
        btnValidate = (Button) findViewById(R.id.btnValidateMessageMessage);
        btnValidate.setOnClickListener(validateListener);

        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBackCreateMessage);
        btnBack.setOnClickListener(backListener);

        // Affichage liste des messages du sujet
        new DisplayMessageOrderByDate(CreateMessageActivity.this).execute(subject);
    }

    // Refresh liste si activité en veille
    @Override
    protected void onRestart() {
        super.onRestart();

        // Affichage liste des messages du sujet
        new DisplayMessageOrderByDate(CreateMessageActivity.this).execute(subject);
    }
}