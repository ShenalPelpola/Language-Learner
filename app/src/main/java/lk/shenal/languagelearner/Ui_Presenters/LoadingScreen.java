package lk.shenal.languagelearner.Ui_Presenters;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.sql.Array;
import java.util.List;
import java.util.Objects;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Models.Language;
import lk.shenal.languagelearner.R;

public class LoadingScreen extends AppCompatActivity {
    private static int SPLASH_TIME = 3000;  //the time of delay
    private DatabaseManager databaseManager;
    private Dialog mDialog;
    private String TAG = "LANGUAGE TABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        mDialog = new Dialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        databaseManager = new DatabaseManager(LoadingScreen.this);
        startApp();
    }

    public class FetchLanguagesService extends AsyncTask<Void, Void, List<IdentifiableLanguage>> {
        final String API_KEY = "9a61nZ4nVwXrxD-0wF-L5j6tq2iTusXTsNCr6IeRdsps";
        final String URL = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/82500469-d6e0-49fb-949d-8720665a12bf";


        @Override
        protected List<IdentifiableLanguage> doInBackground(Void... voids) {
            List<IdentifiableLanguage> fetchedLanguages; //Declaring an identifiableLanguage List
            //Setting up the authentication in ibm services
            Authenticator authenticator = new IamAuthenticator(API_KEY);
            LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
            service.setServiceUrl(URL);

            IdentifiableLanguages languages = service.listIdentifiableLanguages()
                    .execute().getResult();
            fetchedLanguages = languages.getLanguages(); //initializing the identifiableLanguage List
            return fetchedLanguages;
        }

        @Override
        protected void onPostExecute(List<IdentifiableLanguage> fetchedLanguages){
            super.onPostExecute(fetchedLanguages);
            initializeLanguageTable(fetchedLanguages); //Inserts all the fetched languages to the language table

            //if the user already registered user is directed to main activity
            if(databaseManager.isUserLoggedIn()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeIntent = new Intent(LoadingScreen.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }
                }, SPLASH_TIME);
            }else{
                //if the user has'nt registered user is directed to the login activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent loginIntent = new Intent(LoadingScreen.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                }, SPLASH_TIME);
            }
        }
    }

    //This method checks if the connection is available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }


    //This method initialize the languages table
    public void initializeLanguageTable(List<IdentifiableLanguage> fetchedLanguages){
        //if language table isn't initialized before
        if(!databaseManager.checkLanguagesAvailable()){
            for(IdentifiableLanguage fetchedLanguage: fetchedLanguages){
                //Setting up the language image
                String imageName;
                String[] fetchedLanguageName = fetchedLanguage.getName().split(" ");
                if(fetchedLanguageName.length == 2){
                    imageName = fetchedLanguageName[0] + "_" +fetchedLanguageName[1];
                }else{
                    imageName = fetchedLanguageName[0];
                }
                int languageImageResource = getResources().getIdentifier(imageName.trim().toLowerCase(),"drawable", "lk.shenal.languagelearner");
                Language language = new Language(fetchedLanguage.getLanguage(), fetchedLanguage.getName()); //creating a new language object
                language.setCountryImage(languageImageResource); //passing the language object to languages table
                boolean inserted = databaseManager.insertLanguages(language);
                if(inserted){
                    Log.d(TAG, "Inserted successfully");
                }else{
                    Log.d(TAG, "Inserted failed");
                }
            }
        }
    }

    public void startApp(){
        //if the network connection is available app starts and execute the fetch Languages task
        if(isNetworkAvailable()) {
            FetchLanguagesService process = new FetchLanguagesService();
            process.execute();
        }else{
            //Connections error
            showConnectionErrorMessage();
        }
    }

    public void showConnectionErrorMessage(){
        mDialog.setContentView(R.layout.pop_up_internet_error);
        Button internet_error_ok_button = mDialog.findViewById(R.id.internet_error_ok_button);

        internet_error_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }
}
