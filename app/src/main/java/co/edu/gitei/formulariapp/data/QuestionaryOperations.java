package co.edu.gitei.formulariapp.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CONTENIDOS on 16/11/2017.
 */

public class QuestionaryOperations {
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] columns={QuestionaryDBHandler.COLUMN_ID,QuestionaryDBHandler.COLUMN_FORM_REF,QuestionaryDBHandler.COLUMN_ANSWERS};

}
