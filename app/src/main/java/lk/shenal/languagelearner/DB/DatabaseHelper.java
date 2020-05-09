package lk.shenal.languagelearner.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Information
    private static final int DB_VERSION = 1;
    private static String DB_NAME = "Language_Leaner.db";
    //Table Information - Phrases table
    public  static String TABLE_NAME = "phrases";
    public String COLUMN_PHRASE_CONTENT = "phrase_content";
    public String COLUMN_PHRASE_COLOR = "phrase_color";
    public String COLUMN_PHRASE_IMAGE = "phrase_image";
    private String DB_CREATE = "CREATE TABLE "+ TABLE_NAME+" ( "+
            COLUMN_PHRASE_CONTENT+ " text," +
            COLUMN_PHRASE_COLOR+ " text," +
            COLUMN_PHRASE_IMAGE+ " integer);";

    public static String TRANSLATED_PHRASES_TABLE = "translated_phrases";
    public String COLUMN_TRANSLATED_LANGUAGE = "translated_language";
    public String COLUMN_TRANSLATED_PHRASES = "translated_phrases";
    public String DB_CREATE_TRANSLATIONS = "CREATE TABLE IF NOT EXISTS "+ TRANSLATED_PHRASES_TABLE +" ( "+
            COLUMN_TRANSLATED_LANGUAGE+ " text," +
            COLUMN_TRANSLATED_PHRASES+ " text);";

    //Table Information - Languages table
    public static String LANGUAGES_TABLE_NAME = "languages";
    public String COLUMN_LANGUAGE_CODE = "language_code";
    public String COLUMN_LANGUAGE_NAME = "language_name";
    public String COLUMN_LANGUAGE_SUBSCRIPTION = "language_subscription";
    public String COLUMN_LANGUAGE_IMAGE = "language_image";
    private String DB_CREATE_LANGUAGES_TABLE = "CREATE TABLE IF NOT EXISTS "+ LANGUAGES_TABLE_NAME+" ( "+
                        COLUMN_LANGUAGE_CODE+ " text," +
                        COLUMN_LANGUAGE_NAME+ " text," +
                        COLUMN_LANGUAGE_SUBSCRIPTION + " text," +
                        COLUMN_LANGUAGE_IMAGE + " integer);";

    public static String USER_TABLE_NAME = "user_table";
    public String COLUMN_USER_NAME = "user_name";
    public String COLUMN_USER_EMAIL = "email";
    public String COLUMN_USER_PASSWORD = "password";
    private String DB_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "+ USER_TABLE_NAME+" ( "+
            COLUMN_USER_NAME+ " text," +
            COLUMN_USER_EMAIL+ " text," +
            COLUMN_USER_PASSWORD+ " text);";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
        db.execSQL(DB_CREATE_LANGUAGES_TABLE);
        db.execSQL(DB_CREATE_TRANSLATIONS);
        db.execSQL(DB_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LANGUAGES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSLATED_PHRASES_TABLE);
        onCreate(db);
    }
}
