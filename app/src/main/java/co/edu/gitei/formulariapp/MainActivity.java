package co.edu.gitei.formulariapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.edu.gitei.formulariapp.data.DummyData;
import co.edu.gitei.formulariapp.data.Questionary;
import co.edu.gitei.formulariapp.data.QuestionaryOperations;

public class MainActivity extends AppCompatActivity {

    int prueba;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase =database.getReference();
    private String questRef;
    private Questionary questions;
    private QuestionaryOperations questionaryOps;

    private Button btn;

    List<View> allViewInstance = new ArrayList<View>();
    JSONObject jsonObject = new JSONObject();
    private JSONObject optionsObj;
    private final String TAG= "CONTROL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questionaryOps=new QuestionaryOperations(this);
        Toast.makeText(getApplicationContext(), "ATENCION: tienes "+questionaryOps.getAllAnswers().size()+" sin subir", Toast.LENGTH_LONG).show();
        //the intention of this listener is for probe which answers set is updated and delete it from de local DB
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Questionary>formsInDB=questionaryOps.getAllAnswers();
                for (Questionary question: formsInDB ) {
                    if(question.getId()>1) {  //yes, is a dumb if, but i prefer it over have a very large condition below, this if skips the question row (the first one)
                        if (dataSnapshot.child(question.getFormReference()).child(question.getFormCreationIdentifier()).exists()) {
                            questionaryOps.removeQuestionay(question);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        questions=questionaryOps.getQuestionary(1);
        questRef=questions.getFormReference();
        loadForm(questions.getAnswers());
        btn = (Button) findViewById(R.id.button_send_form);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object=getDataFromDynamicViews(v);
                Questionary tempQuestionary=new Questionary();
                tempQuestionary.setFormReference(questRef);
                tempQuestionary.setAnswers(object.toString());
                tempQuestionary.setFormCreationIdentifier(Calendar.getInstance().getTime().toString()+""+ Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                questionaryOps.addAnswer(tempQuestionary);
                //loadForm(questions.getAnswers());
                Toast.makeText(getApplicationContext(), "Formulario guardado correctamente", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(i);
            }

        });
    }


    private void loadForm(String form){
        setContentView(R.layout.activity_main);

        LinearLayout viewProductLayout = (LinearLayout) findViewById(R.id.questionsPanel);


        try {
            jsonObject = new JSONObject(form);
            JSONArray questionsList = jsonObject.getJSONArray("questionary_options");

            for (int noOfQuestions = 0; noOfQuestions < questionsList.length(); noOfQuestions++) {

                JSONObject eachData = questionsList.getJSONObject(noOfQuestions);
                TextView customOptionsName = new TextView(MainActivity.this);
                customOptionsName.setTextSize(18);
                customOptionsName.setPadding(0, 15, 0, 15);
                customOptionsName.setText(eachData.getString("option_name"));
                viewProductLayout.addView(customOptionsName);


//                    /***************************Radio*****************************************************/

                if (eachData.getString("option_type").equals("R")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 3;
                    params.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("variants");
                    RadioGroup rg = new RadioGroup(MainActivity.this); //create the RadioGroup
                    allViewInstance.add(rg);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(MainActivity.this);
                        rg.addView(rb, params);
                        if (j == 0)
                            rb.setChecked(true);
                        rb.setLayoutParams(params);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("variant_name"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString("variant_name");
                        rb.setText(optionString);
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                View radioButton = group.findViewById(checkedId);
                                String variant_name = radioButton.getTag().toString();
                                Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    viewProductLayout.addView(rg, params);
                }

//                    /***********************************CheckBox ***********************************************/


                if (eachData.getString("option_type").equals("C")) {


                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("variants");

                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {
                        CheckBox chk = new CheckBox(MainActivity.this);
                        chk.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        allViewInstance.add(chk);
                        chk.setTag(checkBoxJSONOpt.getJSONObject(j).getString("option_"+(j+1)));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 3;
                        params.bottomMargin = 3;
                        String optionString = checkBoxJSONOpt.getJSONObject(j).getString("option_"+(j+1));
                        chk.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String variant_name = v.getTag().toString();
                                Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                            }
                        });
                        chk.setText(optionString);
                        viewProductLayout.addView(chk, params);
                    }
                }
                if (eachData.getString("option_type").equals("T")) {
                    TextInputLayout til = new TextInputLayout(MainActivity.this);
                    //REVISAR--- NO SEA VAGO
                    //til.setHint(getString(R.string.app_name));
                    EditText et = new EditText(MainActivity.this);
                    til.addView(et);
                    allViewInstance.add(et);
                    viewProductLayout.addView(til);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private boolean uploadAnElementDataInCloudDB(Questionary questionary){
        if(isConnectedToInternet()){
            try {
                mDatabase.child(questionary.getFormReference()).child(questionary.getFormCreationIdentifier()).setValue(new JSONObject(questionary.getAnswers()));
                return true;
            }catch (Exception e){
                Log.e("uploadDataInCloudDB: ", e.toString());
                return false;
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Sin Conexión a internet",Toast.LENGTH_LONG);
        }
        return false;
    }

    private void uploadAllToCloudDB(){
        List<Questionary> answersInDB=questionaryOps.getAllAnswers();
        for(Questionary question: answersInDB){
            if(question.getId()>1) {
                uploadAnElementDataInCloudDB(question);
            }
        }
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager =  (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
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

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                if(isConnectedToInternet()) {
                    uploadAllToCloudDB();
                }else {
                    Toast.makeText(getApplicationContext(),"Sin Conexión a internet",Toast.LENGTH_LONG);
                }
                break;
            case R.id.new_form:
        }
        return true;
    }
*/
    public JSONObject getDataFromDynamicViews(View v) {
        try {
            JSONArray customOptnList = jsonObject.getJSONArray("questionary_options");

            optionsObj = new JSONObject();
            int countCorrection=0;
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);
                if (eachData.getString("option_type").equals("R")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(noOfViews+countCorrection);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    optionsObj.put(eachData.getString("option_name"),
                            "" + selectedRadioBtn.getTag().toString());
                }

                // this if reads all checkbox items and save its answers in a JSON object, after it this object is saved in the main JSON that stores
                // all answers
                if (eachData.getString("option_type").equals("C")) {
                    JSONObject variants= new JSONObject();
                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("variants");
                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {
                        Log.d("REVISION", countCorrection+"");
                        CheckBox tempChkBox = (CheckBox) allViewInstance.get(noOfViews+countCorrection);
                        if (tempChkBox.isChecked()) {
                            variants.put("option_"+(j+1), tempChkBox.getTag().toString());
                        }
                         countCorrection++;
                    }
                    countCorrection--;
                    optionsObj.put(eachData.getString("option_name"),variants);

                }
                if (eachData.getString("option_type").equals("T")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews+countCorrection);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.put(eachData.getString("option_name"), textView.getText().toString());
                    else
                        optionsObj.put(eachData.getString("option_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }
            }

            /*
            String outputData = (optionsObj + "").replace(",", "\n");
            outputData = outputData.replaceAll("[{}]","");
            ((TextView) findViewById(R.id.showData)).setText(outputData);
            Log.d("optionsObj", optionsObj + "");*/

            hideSoftKeyboard(findViewById(R.id.layout));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return optionsObj;
    }

    public void hideSoftKeyboard(View v) {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_form:
                loadForm(questions.getAnswers());
                return true;
            case R.id.load_form:
                Intent i = new Intent(MainActivity.this, AccordionActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }

    }*/

}
