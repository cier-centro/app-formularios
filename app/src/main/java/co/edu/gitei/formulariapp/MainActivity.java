package co.edu.gitei.formulariapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.gitei.formulariapp.data.DummyData;

public class MainActivity extends AppCompatActivity {

    int prueba;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase =database.getReference();


    List<View> allViewInstance = new ArrayList<View>();
    JSONObject jsonObject = new JSONObject();
    private JSONObject optionsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LinearLayout viewProductLayout = (LinearLayout) findViewById(R.id.questionsPanel);

        Button btn = (Button) findViewById(R.id.button_send_form);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getDataFromDynamicViews(v);
            }
        });

        try {
            jsonObject = new JSONObject(DummyData.dummyData);
            JSONArray questionsList = jsonObject.getJSONArray("product_options");

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
                        chk.setTag(checkBoxJSONOpt.getJSONObject(j).getString("variant_name"));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 3;
                        params.bottomMargin = 3;
                        String optionString = checkBoxJSONOpt.getJSONObject(j).getString("variant_name");
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
                    til.setHint(getString(R.string.app_name));
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


    public void getDataFromDynamicViews(View v) {
        try {
            JSONArray customOptnList = jsonObject.getJSONArray("product_options");

            optionsObj = new JSONObject();
            int countCorrection=0;
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);
                /*Log.d("ESTO a evaluar",eachData.getString("option_type"));
                Log.d("ESTO TAMBIEN",allViewInstance.get(noOfViews).toString());*/
                if (eachData.getString("option_type").equals("R")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(noOfViews+countCorrection);
                    Log.d("LOG", "si puedo hacer casting a RADIOGROUP");
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    optionsObj.put(eachData.getString("option_name"),
                            "" + selectedRadioBtn.getTag().toString());
                }

                if (eachData.getString("option_type").equals("C")) {
                    boolean allOptionReviewed=false;
                    while (!allOptionReviewed) {
                        Log.d("REVISION", countCorrection+"");
                        CheckBox tempChkBox = (CheckBox) allViewInstance.get(noOfViews+countCorrection);
                        if (tempChkBox.isChecked()) {
                            optionsObj.put(eachData.getString("option_name"), tempChkBox.getTag().toString());
                        }

                        Log.d("variant_name", tempChkBox.getTag().toString());
                        try{
                            countCorrection++;
                            Object ob =customOptnList.getJSONObject(noOfViews+countCorrection).getString("option_type");
                        }catch (Exception e){
                            countCorrection--;
                            allOptionReviewed=true;
                        }
                    }
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

            String outputData = (optionsObj + "").replace(",", "\n");
            outputData = outputData.replaceAll("[{}]","");
            ((TextView) findViewById(R.id.showData)).setText(outputData);
            Log.d("optionsObj", optionsObj + "");

            hideSoftKeyboard(findViewById(R.id.layout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard(View v) {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
