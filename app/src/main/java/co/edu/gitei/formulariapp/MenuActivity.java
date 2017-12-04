package co.edu.gitei.formulariapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.edu.gitei.formulariapp.data.DummyData;
import co.edu.gitei.formulariapp.data.Questionary;
import co.edu.gitei.formulariapp.data.QuestionaryOperations;

/**
 * Created by sebas on 30/11/2017.
 */

public class MenuActivity extends Activity {
    Button loadForms;
    Button seeSaved;
    Button switchQuestions;

    private int numFormsInDB;
    private QuestionaryOperations questionaryOps;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        questionaryOps=new QuestionaryOperations(this);
        numFormsInDB=questionaryOps.getAllAnswers().size();

        if(numFormsInDB<1){

            questionaryOps.addAnswer(new Questionary("dummy","dummy", DummyData.dummyData));
        }

        loadForms=(Button) findViewById(R.id.load_form);
        loadForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        seeSaved=(Button) findViewById(R.id.see_saved_form);
        seeSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, SeeAll.class);
                startActivity(i);
                //Toast.makeText(getApplicationContext(), "En Desarrollo", Toast.LENGTH_LONG).show();
            }
        });
        switchQuestions=(Button) findViewById(R.id.switch_questions);
        switchQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AccordionActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager =  (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
