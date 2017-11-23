package co.edu.gitei.formulariapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CONTENIDOS on 16/11/2017.
 */

public class QuestionaryDBHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION=1;

    public static final String TABLE_ANSWERS="answers";
    public static final String COLUMN_ID = "formularyID";
    public static final String COLUMN_FORM_REF ="formReference";
    public static final String COLUMN_ANSWERS = "answers";

    private static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_ANSWERS
            +" (" +COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FORM_REF +" TEXT, "+COLUMN_ANSWERS+" TEXT )";


    public QuestionaryDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if ( newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
            sqLiteDatabase.execSQL(TABLE_CREATE);
        }
    }


}
