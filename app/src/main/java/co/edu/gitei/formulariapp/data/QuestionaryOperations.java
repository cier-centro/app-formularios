package co.edu.gitei.formulariapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CONTENIDOS on 16/11/2017.
 */

public class QuestionaryOperations {
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;
    public static final String LOGTAG = "QUEST_MNGMNT_SYS";

    private static final String[] columns={QuestionaryDBHandler.COLUMN_ID,QuestionaryDBHandler.COLUMN_FORM_REF,QuestionaryDBHandler.COLUMN_ANSWERS};

    public QuestionaryOperations(Context context){
        dbhandler = new QuestionaryDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    public Questionary addAnswer(Questionary questionary){
        ContentValues values  = new ContentValues();
        values.put(QuestionaryDBHandler.COLUMN_FORM_REF,questionary.getFormReference());
        values.put(QuestionaryDBHandler.COLUMN_ANSWERS,questionary.getAnswers());
        long insertid = database.insert(QuestionaryDBHandler.TABLE_ANSWERS,null,values);
        questionary.setId(insertid);
        return questionary;
    }


    //get all answers
    public List<Questionary> getAllAnswers(){
        Cursor cursor = database.query(QuestionaryDBHandler.TABLE_ANSWERS,columns,null,null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        List<Questionary> answersList=new ArrayList<>();
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                Questionary questionary = new Questionary();
                questionary.setId(cursor.getLong(cursor.getColumnIndex(QuestionaryDBHandler.COLUMN_ID)));
                questionary.setFormReference(cursor.getString(cursor.getColumnIndex(QuestionaryDBHandler.COLUMN_FORM_REF)));
                questionary.setAnswers(cursor.getString(cursor.getColumnIndex(QuestionaryDBHandler.COLUMN_ANSWERS)));
                answersList.add(questionary);
            }
        }
        // return Employee
        return answersList;
    }

    //delete answer
    public void removeEmployee(Questionary employee) {

        database.delete(QuestionaryDBHandler.TABLE_ANSWERS, QuestionaryDBHandler.COLUMN_ID + "=" + employee.getId(), null);
    }

}