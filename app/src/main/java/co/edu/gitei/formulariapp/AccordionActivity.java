package co.edu.gitei.formulariapp;

/**
 * Created by CONTENIDOS on 27/11/2017.
 */

//MyTable.java
        import android.app.Activity;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.Gravity;
        import android.view.View;
        import android.view.ViewGroup.LayoutParams;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TableLayout;
        import android.widget.TextView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.gson.Gson;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.Iterator;
        import java.util.List;

        import co.edu.gitei.formulariapp.data.Questionary;

public class AccordionActivity extends Activity {
    LinearLayout forms;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase =database.getReference();
    JSONArray jsonArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        forms =(LinearLayout)findViewById(R.id.linearLayout_forms);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("PRE CONTROL", "--------------------------------------------------------------------------------------------------------------------");
                Gson gson= new Gson();
                String array=gson.toJson(dataSnapshot.child("MainForms").getValue());
                //array="{\"MainForms\":"+array+"}";
                try {
                    JSONObject jsonObject=new JSONObject(array);
                    Iterator iterator=jsonObject.keys();
                    jsonArray= new JSONArray();
                    while (iterator.hasNext()){
                        String key=(String) iterator.next();
                        Log.d("CONTROL ",key);
                        jsonArray.put(jsonObject.get(key));
                    }
                    Log.d("CONTROL ",jsonArray.toString());
                    createFormsAccordions();
                } catch (Exception e){
                    Log.d("EXCEPTION", "FUCK THIS");
                }
                Log.d("POS CONTROL", "--------------------------------------------------------------------------------------------------------------------");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void createFormsAccordions() {


        LinearLayout viewProductLayout = (LinearLayout) findViewById(R.id.linearLayout_forms);
        viewProductLayout.removeAllViews();
        try{
        for (int i = 0; i < jsonArray.length(); i++) {
            Button b1;
            TextView t1;
            TextView t2;
            b1 = new Button(this);
            t1 = new TextView(this);
            t2 = new TextView(this);

            b1.setId(i);
            t1.setId(i);


            t1.setText(jsonArray.getJSONObject(i).getString("name"));
            t2.setText(jsonArray.getJSONObject(i).getString("description"));
            b1.setText("Seleccionar formulario");

            t1.setTextSize(20);
            t2.setTextSize(15);
            b1.setTextSize(15);
            Log.d("ERROR QUE NI IDEA", "ERROR QUE NI IDEA Y YA ME SABE A ...");
            b1.setGravity(Gravity.LEFT);
            t1.setGravity(Gravity.LEFT);
            t2.setGravity(Gravity.LEFT);

            viewProductLayout.addView(t1);
            viewProductLayout.addView(t2);
            viewProductLayout.addView(b1);
        }
    }catch(Exception e){

    }
    }
}

