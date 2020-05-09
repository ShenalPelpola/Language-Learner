package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Helper_adapters.TranslationRecyclerViewAdapter;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.Models.TranslatedPhrases;
import lk.shenal.languagelearner.R;

public class TranslateActivity extends AppCompatActivity {
    private static final String IS_ALL_TRANSLATED = "IS_ALL_TRANSLATED";
    private static final String IS_TRANSLATED = "IS_TRANSLATED";
    private static final String TRANSLATED_PHRASE = "TRANSLATED_PHRASE";
    private static final String ENGLISH_PHRASE = "ENGLISH_PHRASE";
    private static final String TRANSLATED_LANGUAGE = "TRANSLATED_LANGUAGE";
    private static final String CLICKED_PHRASE = "CLICKED_PHRASE";
    private Spinner subscriptionLanguageSpinner;
    private TranslationRecyclerViewAdapter mTranslateAdapter;
    private Button translateLanguageButton;
    private Button translateAllButton;
    private DatabaseManager databaseManager;
    private List<Phrase> phrases;
    private LanguageTranslator translationService;
    private List<String> mLanguageNames;
    private Dialog mDialog;
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;
    private Button pronounsButton;
    private String clickedPhrase;
    private boolean isAllTranslatedMessageShown = false;
    private boolean isTranslationMessageShown = false;
    private String translatedPhrase = null;
    private String englishPhrase = null;
    private String language = null;
    private String allTranslatedLanguage = null;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_ALL_TRANSLATED, isAllTranslatedMessageShown);
        outState.putBoolean(IS_TRANSLATED, isTranslationMessageShown);
        outState.putString("language name_All", allTranslatedLanguage);
        outState.putString(CLICKED_PHRASE, clickedPhrase);
        outState.putString(TRANSLATED_PHRASE, translatedPhrase);
        outState.putString(ENGLISH_PHRASE, englishPhrase);
        outState.putString(TRANSLATED_LANGUAGE, language);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isAllTranslatedMessageShown = savedInstanceState.getBoolean(IS_ALL_TRANSLATED);
        isTranslationMessageShown = savedInstanceState.getBoolean(IS_TRANSLATED);
        clickedPhrase = savedInstanceState.getString(CLICKED_PHRASE);
        allTranslatedLanguage = savedInstanceState.getString("language name_All");
        translatedPhrase = savedInstanceState.getString(TRANSLATED_PHRASE);
        englishPhrase = savedInstanceState.getString(ENGLISH_PHRASE);
        language = savedInstanceState.getString(TRANSLATED_LANGUAGE);
        if(isAllTranslatedMessageShown) {
            showAllTranslationsSuccess(allTranslatedLanguage);
        }if(isTranslationMessageShown){
            showSuccess(englishPhrase, language, translatedPhrase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        //initializing the views
        translateLanguageButton = findViewById(R.id.translateLanguageButton);
        subscriptionLanguageSpinner = findViewById(R.id.subscriptionLanguageSpinner);
        translateAllButton = findViewById(R.id.translateAllButton);
        pronounsButton = findViewById(R.id.pronounsButton);
        mDialog = new Dialog(this);
        //Disabling the dialog box closing when touching the window
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        databaseManager = new DatabaseManager(TranslateActivity.this);
        //initializing the services - setting up credentials
        translationService = initLanguageTranslatorService();
        textService = initTextToSpeechService();
        phrases = databaseManager.getAllPhrases(); // initializing the phrases array with phrases from the db
        initializeRecyclerView();
        initializeSpinner();
        pronounsButton.setEnabled(false);

        //checking the connection when opening the activity  is available if not error message is shown
        if(!isNetworkAvailable()){
            showConnectionErrorMessage();
        }else {
            //if the translate all button is clicked TranslationTask() is executed
            translateLanguageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedPhrase = mTranslateAdapter.getClickedPhrase();
                    if (clickedPhrase != null && subscriptionLanguageSpinner != null) {
                        if(isNetworkAvailable()) { //checking if network available before the translation
                            new TranslationTask().execute(clickedPhrase, getSelectedLanguage());
                        }else{
                            showConnectionErrorMessage();
                        }
                    }else if(subscriptionLanguageSpinner == null){
                        showSpinnerEmpty();
                    }else {
                        Toast.makeText(TranslateActivity.this, "Select a phrase", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            translateAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TranslateAllTask().execute(getSelectedLanguage());
                }
            });

            pronounsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()) {
                        new PronounsTask().execute(translatedPhrase);
                    }else{
                        showConnectionErrorMessage();
                    }
                    pronounsButton.setEnabled(false);
                    pronounsButton.setAlpha(0.5f);
                }
            });
        }
    }

    //This method initializes the recyclerview
    public void initializeRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.translationPhraseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(TranslateActivity.this));
        mTranslateAdapter = new TranslationRecyclerViewAdapter(this, phrases);
        recyclerView.setAdapter(mTranslateAdapter);
    }
    //This method initializes the spinner
    public void initializeSpinner(){
        mLanguageNames = databaseManager.getSubscribedLanguageNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TranslateActivity.this, android.R.layout.simple_spinner_item, mLanguageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subscriptionLanguageSpinner.setAdapter(adapter);
    }

    //get the selected value from spinner
    public String getSelectedLanguage(){
        return subscriptionLanguageSpinner.getSelectedItem().toString();
    }

    //Setting up the credentials needed to use ibm translation service
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator("9a61nZ4nVwXrxD-0wF-L5j6tq2iTusXTsNCr6IeRdsps");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/82500469-d6e0-49fb-949d-8720665a12bf");
        return service;
    }
    //Setting up the credentials needed to use ibm pronouns service
    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.text_speech_apikey));
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl(getString(R.string.text_speech_url));
        return service;
    }


    private class TranslationTask extends AsyncTask<String, Void, HashMap<String, String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            HashMap<String, String> translationResults = new HashMap<>();
            try {
                TranslateOptions translateOptions = new TranslateOptions.Builder().addText(params[0])
                        .source(Language.ENGLISH)
                        .target(params[1])
                        .build();
                TranslationResult result = translationService.translate(translateOptions).execute().getResult();
                String firstTranslation = result.getTranslations().get(0).getTranslation();

                translationResults.put("english_phrase", params[0]);
                translationResults.put("language", params[1]);
                translationResults.put("translated_phrase", firstTranslation);
            //if the language not found exception is called
            }catch(NotFoundException e){
                Log.e("translation_error", "Language not available");
            }
            return translationResults;
        }
        @Override
        protected void onPostExecute(HashMap<String, String> translationResults) {
            super.onPostExecute(translationResults);
            if(translationResults.isEmpty()){
                showLanguageEmptyError();
            }else {
                translatedPhrase = translationResults.get("translated_phrase");
                englishPhrase = translationResults.get("english_phrase");
                language = translationResults.get("language");
                showSuccess(englishPhrase, language, translatedPhrase);
                pronounsButton.setEnabled(true);
                pronounsButton.setAlpha(1);
            }
        }
    }

    private class TranslateAllTask extends AsyncTask<String, Void, TranslatedPhrases> {
        @Override
        protected TranslatedPhrases doInBackground(String... params) {
            TranslatedPhrases translatedPhrases = new TranslatedPhrases(params[0]);
            try {
                List<String> foreignPhrases = new ArrayList<>();
                for (Phrase phrase : phrases) {
                    TranslateOptions translateOptions = new TranslateOptions.Builder().addText(phrase.getPhraseContent())
                            .source(Language.ENGLISH)
                            .target(params[0])
                            .build();
                    TranslationResult result = translationService.translate(translateOptions).execute().getResult();
                    String translatedWord = result.getTranslations().get(0).getTranslation();
                    foreignPhrases.add(translatedWord);
                }
                translatedPhrases.setForeignPhrases(foreignPhrases);
            }catch(NotFoundException e){
                Log.e("language_not_found", "Language not available");
            }
            return  translatedPhrases;
        }

        @Override
        protected void onPostExecute(TranslatedPhrases translatedPhrases) {
            super.onPostExecute(translatedPhrases);
            if(translatedPhrases.getForeignPhrases() == null){
                showLanguageEmptyError();
            }else {
                allTranslatedLanguage = translatedPhrases.getTranslatedLanguage();
                boolean isInserted = databaseManager.insertTranslatedPhrases(translatedPhrases);
                if (isInserted) {
                    Log.d("test", "Inserted");
                    showAllTranslationsSuccess(allTranslatedLanguage);
                } else {
                    //if translations already present it updates the table
                    boolean isUpdated = databaseManager.updateTranslations(translatedPhrases);
                    if (isUpdated) {
                        showAllTranslationsSuccess(allTranslatedLanguage);
                        Log.d("test", "updated");
                    } else {
                        Log.d("tesi", "Not updated");
                    }
                }
            }
        }

    }

    private class PronounsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new
                    SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute().getResult());
            return "Did pronouns";
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    public void showSuccess(String englishPhrase, String language, String translatedPhrase){
        isTranslationMessageShown = true;
        mDialog.setContentView(R.layout.translated_message_popup);

        Button button_ok = mDialog.findViewById(R.id.ok_button);
        TextView translatedMessageTextView = mDialog.findViewById(R.id.translatedMessageTextView);
        translatedMessageTextView.setText("Your Phrase in english is " + englishPhrase + "\n When translated to " +language + " is " + translatedPhrase);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTranslationMessageShown = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void showSpinnerEmpty(){
        mDialog.setContentView(R.layout.pop_up_message);

        Button button_ok = mDialog.findViewById(R.id.ok_button);
        TextView translatedMessageTextView = mDialog.findViewById(R.id.translatedMessageTextView);
        translatedMessageTextView.setText("You haven't subscribed to any language");
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void showLanguageEmptyError(){
        mDialog.setContentView(R.layout.pop_up_message);

        Button button_ok = mDialog.findViewById(R.id.ok_button);
        TextView translatedMessageTextView = mDialog.findViewById(R.id.translatedMessageTextView);
        translatedMessageTextView.setText("That language is not available at the moment");
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void showAllTranslationsSuccess(String languageName){
        isAllTranslatedMessageShown = true;
        mDialog.setContentView(R.layout.pop_up_message);

        Button button_ok = mDialog.findViewById(R.id.ok_button);
        TextView translatedMessageTextView = mDialog.findViewById(R.id.translatedMessageTextView);
        translatedMessageTextView.setText("All your phrases are translated to " +languageName +" you can view them in your translations!");
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllTranslatedMessageShown = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    public void showConnectionErrorMessage(){
        mDialog.setContentView(R.layout.pop_up_internet_error);
        Button internet_error_ok_button = mDialog.findViewById(R.id.internet_error_ok_button);
        TextView internet_error_message = mDialog.findViewById(R.id.internet_error_message);
        internet_error_message.setText("App ran in to a problem. Please check your internet connection!");

        internet_error_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }
}
