package co.edu.gitei.formulariapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

                if (eachData.getString("option_type").equals("S")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("variants");
                    ArrayList<String> SpinnerOptions = new ArrayList<String>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        String optionString = dropDownJSONOpt.getJSONObject(j).getString("variant_name");
                        SpinnerOptions.add(optionString);
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = null;
                    spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spiner_row, SpinnerOptions);
                    Spinner spinner = new Spinner(MainActivity.this);
                    allViewInstance.add(spinner);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setSelection(0, false);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            try {
                                String variant_name = dropDownJSONOpt.getJSONObject(position).getString("variant_name");
                                Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });
                    viewProductLayout.addView(spinner);
                }

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

                        if (!(checkBoxJSONOpt.getJSONObject(j).getString("variant_name").equalsIgnoreCase("NO"))) {
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


    }
