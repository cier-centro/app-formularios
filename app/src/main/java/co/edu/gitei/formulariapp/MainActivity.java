package co.edu.gitei.formulariapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    int prueba;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase =database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null) {
            prueba=0;
        }
        else {
            prueba = savedInstanceState.getInt("stateCount");
        }
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.button_send_form);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prueba++;
                mDatabase.child("pruebaDeVida").child("prueba1").setValue(prueba);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("stateCount",prueba);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        prueba=savedInstanceState.getInt("stateCount");
    }

    }
