package lk.shenal.languagelearner.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lk.shenal.languagelearner.Models.Language;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.Models.TranslatedPhrases;
import lk.shenal.languagelearner.Models.User;
import lk.shenal.languagelearner.R;

//Perform all crud operations
public class DatabaseManager {
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper; // Helper class for creating and opening the DB
    private Context mContext;

    public  DatabaseManager(Context context){
        mContext = context;
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    /*
     * Open the db. Will create if it doesn't exist
     */
    public void open(){
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    /*
     *  Closing our db connections after performing crud operations
     */
    public void close() {
        mDatabase.close();
    }

    /*
     * CRUD operations!
     */

    /*
     * INSERT
     */

    public boolean insertNewUser(User user){
        open();
        ContentValues contentValues = new ContentValues();
        String userName = user.getUserName();
        String email = user.getEmail();
        String password = user.getPassword();
        contentValues.put(mDatabaseHelper.COLUMN_USER_NAME, userName);
        contentValues.put(mDatabaseHelper.COLUMN_USER_EMAIL, email);
        contentValues.put(mDatabaseHelper.COLUMN_USER_PASSWORD, password);
        long result = mDatabase.insert(mDatabaseHelper.USER_TABLE_NAME, null, contentValues);
        close();
        return result != -1;

    }


    public boolean insertNewPhrase(Phrase phrase){
        open();
        ContentValues contentValues = new ContentValues();
        String phraseContent = phrase.getPhraseContent();
        String phraseColor = phrase.getPhraseColor();
        int phraseImage = phrase.getPhraseImage();

        boolean isPresent = isPhraseAvailable(phraseContent);

        if(isPresent){
            return false;
        }else {
            contentValues.put(mDatabaseHelper.COLUMN_PHRASE_CONTENT, phraseContent);
            contentValues.put(mDatabaseHelper.COLUMN_PHRASE_COLOR, phraseColor);
            contentValues.put(mDatabaseHelper.COLUMN_PHRASE_IMAGE, phraseImage);
            long result = mDatabase.insert(mDatabaseHelper.TABLE_NAME, null, contentValues);
            close();
            return result != -1;
        }
    }


    public boolean insertLanguages(Language language){
        open();
        ContentValues contentValues = new ContentValues();
        String languageCode = language.getLanguageCode();
        String languageName = language.getLanguageName();
        String isSubscribed = Boolean.toString(language.getIsSubscribed());
        int languageImage = language.getCountryImage();


        contentValues.put(mDatabaseHelper.COLUMN_LANGUAGE_CODE, languageCode);
        contentValues.put(mDatabaseHelper.COLUMN_LANGUAGE_NAME, languageName);
        contentValues.put(mDatabaseHelper.COLUMN_LANGUAGE_SUBSCRIPTION, isSubscribed);
        contentValues.put(mDatabaseHelper.COLUMN_LANGUAGE_IMAGE, languageImage);
        long result = mDatabase.insert(mDatabaseHelper.LANGUAGES_TABLE_NAME, null, contentValues);
        close();
        return result != -1;
    }

    public boolean insertTranslatedPhrases(TranslatedPhrases translatedPhrases){
        open();
        ContentValues contentValues = new ContentValues();
        String languageName = translatedPhrases.getTranslatedLanguage();

        if(!isAlreadyTranslated(languageName)) {
            List<String> translatedForeignPhrases = translatedPhrases.getForeignPhrases();
            StringBuilder stringTranslatedPhrase = new StringBuilder();

            for (int i = 0; i < translatedForeignPhrases.size(); i++) {
                stringTranslatedPhrase.append(translatedForeignPhrases.get(i));
                if (i < translatedForeignPhrases.size() - 1) {
                    stringTranslatedPhrase.append(",");
                }
            }
            contentValues.put(mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE, languageName);
            contentValues.put(mDatabaseHelper.COLUMN_TRANSLATED_PHRASES, stringTranslatedPhrase.toString());
            long result = mDatabase.insert(mDatabaseHelper.TRANSLATED_PHRASES_TABLE, null, contentValues);
            close();
            return result != -1;
        }else{
            return false;
        }
    }

    /*
     * READ - SELECT ALL from THE phrase table
     */
    public ArrayList<Phrase> getAllPhrases(){
        ArrayList<Phrase> phrases = new ArrayList<>();
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_PHRASE_CONTENT, mDatabaseHelper.COLUMN_PHRASE_COLOR, mDatabaseHelper.COLUMN_PHRASE_IMAGE};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TABLE_NAME,
                columnNames, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                mDatabaseHelper.COLUMN_PHRASE_CONTENT +" ASC "  // orderby
        );
        while(cursor.moveToNext()){
            Phrase phrase = new Phrase(cursor.getString(0),cursor.getString(1), cursor.getInt(2));
            phrases.add(phrase);
        }
        return phrases;
    }

    public ArrayList<TranslatedPhrases> getAllTranslatedPhrases(){
        ArrayList<TranslatedPhrases> translatedPhrases = new ArrayList<>();
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE, mDatabaseHelper.COLUMN_TRANSLATED_PHRASES};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TRANSLATED_PHRASES_TABLE,
                columnNames, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null
        );
        while(cursor.moveToNext()){
            List<String> phrases = Arrays.asList(cursor.getString(1).split(","));
            TranslatedPhrases translatedPhrase = new TranslatedPhrases(cursor.getString(0));
            translatedPhrase.setForeignPhrases(phrases);
            translatedPhrases.add(translatedPhrase);
        }
        return translatedPhrases;
    }

    public ArrayList<String> getAllTranslatedLanguageNames(){
        ArrayList<String> languageNames = new ArrayList<>();
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TRANSLATED_PHRASES_TABLE,
                columnNames,
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){
            languageNames.add(cursor.getString(0));
        }
        return languageNames;
    }

    public User getUser(){
        open();
        User user = null;
        String[] columnNames = {mDatabaseHelper.COLUMN_USER_NAME, mDatabaseHelper.COLUMN_USER_EMAIL, mDatabaseHelper.COLUMN_USER_PASSWORD};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.USER_TABLE_NAME,
                columnNames, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null
        );
        while(cursor.moveToNext()){
            user = new User(cursor.getString(0),cursor.getString(1), cursor.getString(2));
        }
        return user;
    }

    /*
     * READ - SELECT WHERE from THE phrase table - check availability of a phrase
     */
    public boolean isPhraseAvailable(String phraseContent) {
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_PHRASE_CONTENT};
        String whereClause = mDatabaseHelper.COLUMN_PHRASE_CONTENT + " LIKE ?";
        String[] whereParams = {phraseContent};

        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TABLE_NAME,
                columnNames, // column names
                whereClause, // where clause
                whereParams, //  where params
                null, // groupby
                null, // having
                mDatabaseHelper.COLUMN_PHRASE_CONTENT + " ASC "  // orderby
        );
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean isAlreadyTranslated(String languageName) {
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE};
        String whereClause = mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE + " LIKE ?";
        String[] whereParams = {languageName};

        Cursor cursor = mDatabase.query(
                mDatabaseHelper.TRANSLATED_PHRASES_TABLE,
                columnNames, // column names
                whereClause, // where clause
                whereParams, //  where params
                null, // groupby
                null, // having
                null// orderby
        );
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Boolean isUserLoggedIn(){
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_USER_NAME, mDatabaseHelper.COLUMN_USER_EMAIL, mDatabaseHelper.COLUMN_USER_PASSWORD};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.USER_TABLE_NAME,
                columnNames, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null
        );
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkLanguagesAvailable(){
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_LANGUAGE_CODE, mDatabaseHelper.COLUMN_LANGUAGE_NAME, mDatabaseHelper.COLUMN_LANGUAGE_SUBSCRIPTION};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.LANGUAGES_TABLE_NAME,
                columnNames,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Language> getLanguages(){
        ArrayList<Language> languages = new ArrayList<>();
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_LANGUAGE_CODE, mDatabaseHelper.COLUMN_LANGUAGE_NAME, mDatabaseHelper.COLUMN_LANGUAGE_SUBSCRIPTION, mDatabaseHelper.COLUMN_LANGUAGE_IMAGE};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.LANGUAGES_TABLE_NAME,
                columnNames,
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){
            Language language = new Language(cursor.getString(0), cursor.getString(1));
            language.setIsSubscribed(Boolean.parseBoolean(cursor.getString(2)));
            language.setCountryImage(cursor.getInt(3));
            languages.add(language);
        }
        return languages;
    }

    public ArrayList<String> getSubscribedLanguageNames(){
        ArrayList<String> languageNames = new ArrayList<>();
        open();
        String[] columnNames = {mDatabaseHelper.COLUMN_LANGUAGE_NAME};
        String whereClause = mDatabaseHelper.COLUMN_LANGUAGE_SUBSCRIPTION + " LIKE ?";
        String[] whereParams = {"true"};
        Cursor cursor = mDatabase.query(
                mDatabaseHelper.LANGUAGES_TABLE_NAME,
                columnNames,
                whereClause,
                whereParams,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){
            languageNames.add(cursor.getString(0));
        }
        return languageNames;
    }

    /*
     * UPDATE - UPDATE the select phrase
     */
    public boolean updatePhrase(String phraseContent, String updatePhraseContent){
        open();
        ContentValues contentValues = new ContentValues();

        boolean isPresent = isPhraseAvailable(updatePhraseContent);
        if(isPresent){
            return false;
        }else {
            contentValues.put(mDatabaseHelper.COLUMN_PHRASE_CONTENT, updatePhraseContent);
            long result = mDatabase.update(mDatabaseHelper.TABLE_NAME, contentValues, mDatabaseHelper.COLUMN_PHRASE_CONTENT + " = ? ", new String[]{phraseContent});
            close();
            return result != -1;
        }
    }

    public boolean updateLanguages(String languageName, boolean isChecked){
        open();
        ContentValues contentValues = new ContentValues();
        String whereClause = mDatabaseHelper.COLUMN_LANGUAGE_NAME + " LIKE ?";
        String[] whereParams = {languageName};
        contentValues.put(mDatabaseHelper.COLUMN_LANGUAGE_SUBSCRIPTION, Boolean.toString(isChecked));
        long isUpdate= mDatabase.update(mDatabaseHelper.LANGUAGES_TABLE_NAME, contentValues, whereClause, whereParams);
        return isUpdate != -1;
    }

    public boolean updateTranslations(TranslatedPhrases translatedPhrases){
        open();
        ContentValues contentValues = new ContentValues();
        List<String> translatedForeignPhrases = translatedPhrases.getForeignPhrases();
        StringBuilder stringTranslatedPhrase = new StringBuilder();

        String whereClause = mDatabaseHelper.COLUMN_TRANSLATED_LANGUAGE + " LIKE ?";
        String[] whereParams = {translatedPhrases.getTranslatedLanguage()};


        for (int i = 0; i < translatedForeignPhrases.size(); i++) {
            stringTranslatedPhrase.append(translatedForeignPhrases.get(i));
            if (i < translatedForeignPhrases.size() - 1) {
                stringTranslatedPhrase.append(",");
            }
        }

        contentValues.put(mDatabaseHelper.COLUMN_TRANSLATED_PHRASES, String.valueOf(stringTranslatedPhrase));
        long isUpdate= mDatabase.update(mDatabaseHelper.TRANSLATED_PHRASES_TABLE, contentValues, whereClause, whereParams);
        return isUpdate != -1;
    }
}
