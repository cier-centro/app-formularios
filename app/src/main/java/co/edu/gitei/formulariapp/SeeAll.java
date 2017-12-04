package co.edu.gitei.formulariapp;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import co.edu.gitei.formulariapp.data.Questionary;
import co.edu.gitei.formulariapp.data.QuestionaryOperations;


/**
 * Created by CONTENIDOS on 04/12/2017.
 */

public class SeeAll extends ListActivity{

    private QuestionaryOperations questionaryOps;
    private List<Questionary> questionaries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_in_db);

        questionaryOps = new QuestionaryOperations(this);
        questionaries = questionaryOps.getAllAnswers();
        ArrayAdapter<Questionary> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, questionaries);
        setListAdapter(adapter);
    }
}
