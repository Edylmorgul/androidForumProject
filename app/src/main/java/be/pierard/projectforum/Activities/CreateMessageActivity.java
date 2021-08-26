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
import be.pierard.projectforum.Data.Global;
import be.pierard.projectforum.Data.Message;
import be.pierard.projectforum.Data.Subject;
import be.pierard.projectforum.Data.User;
import be.pierard.projectforum.R;

public class CreateMessageActivity extends AppCompatActivity {

    // Data
    private User userCurrent = new User();
    private User user = new User();
    private Subject subject = new Subject();
    private Message message;
    private LinearLayout layout;
    public List<Message> list;
    EditText content;

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
            textView.setText(" DATE : " + m.getDateMessage() + " " + getResources().getString(R.string.authorName) + " " + m.getUser().getPseudo() + " : " + m.getContent());
            layout.addView(textView);
            textView.setId(i);
            i++;
        }
    }

    // Creéation objet message
    private View.OnClickListener validateListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if(!checkData()){
                try{
                    // Creation objet message
                    // Récup date actuelle message
                    Date currentTime = new Date();
                    message = new Message(content.getText().toString(),currentTime, userCurrent);
                    new CreateMessageAsync(CreateMessageActivity.this, subject).execute(message);
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), CreateMessageActivity.this);
                }
            }
        }
    };

    // Vérifier les champs
    public boolean checkData(){
        // Données
        boolean error = false;

        // Récupération des champs
        content = findViewById(R.id.messageTextSubjectByUser);

        // Test des différents champs
        if(content.getText().toString().isEmpty()){
            content.setError(getResources().getString(R.string.errorForm));
            error = true;
        }

        return error;
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
            Global.generateToast(getResources().getString(R.string.toastErrorName), CreateMessageActivity.this);
        }
    };

    // Recu reponse CreateMessageAsync
    public void response(Message param, int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), CreateMessageActivity.this);
                break;
            case 201 :
                try{
                    Global.generateToast(getResources().getString(R.string.toastValidateName), CreateMessageActivity.this);
                    // Refresh de la liste des messages
                    new DisplayMessageOrderByDate(CreateMessageActivity.this).execute(subject);
                    populateListMessage();
                    EditText textMessage = findViewById(R.id.messageTextSubjectByUser);
                    // Vide textView
                    textMessage.setText("");
                }
                catch (Exception e){
                    Global.generateToast(getResources().getString(R.string.toastErrorName), CreateMessageActivity.this);
                }
                break;
            case 401 :
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), CreateMessageActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), CreateMessageActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), CreateMessageActivity.this);
                break;
        }
    }

    // Recu reponse DisplayMessageBySubjectAsync
    @SuppressLint("ResourceType")
    public void response(int code) {
        switch (code){
            case 1 :
                Global.generateToast(getResources().getString(R.string.toastErrorName), CreateMessageActivity.this);
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
                Global.generateToast(getResources().getString(R.string.toastErrorSqlLiteName), CreateMessageActivity.this);
                break;
            case 403 :
                Global.generateToast(getResources().getString(R.string.toastErrorBddNotFoundName), CreateMessageActivity.this);
                break;
            case 500 :
                Global.generateToast(getResources().getString(R.string.toastErrorServerName), CreateMessageActivity.this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        // Récupération informations activitée précédente
        Intent intentRecupUserCurrent = getIntent();
        userCurrent = (User) intentRecupUserCurrent.getSerializableExtra("ObjectUserCurrent");
        Intent intentRecupUser = getIntent();
        user = (User) intentRecupUser.getSerializableExtra("ObjectUser");
        Intent intentRecupSubject = getIntent();
        subject = (Subject) intentRecupSubject.getSerializableExtra("ObjectSubject");

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