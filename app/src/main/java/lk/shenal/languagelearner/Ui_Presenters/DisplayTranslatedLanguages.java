
package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Helper_adapters.ForeignPhrasesRecyclerViewAdapter;
import lk.shenal.languagelearner.Models.TranslatedPhrases;
import lk.shenal.languagelearner.R;

public class DisplayTranslatedLanguages extends AppCompatActivity {
    private Spinner translatedLanguageSpinner;
    private DatabaseManager databaseManager;
    private ArrayList<String> translatedLanguages;
    private List<TranslatedPhrases> translatedPhrases;
    private ForeignPhrasesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing the views
        setContentView(R.layout.activity_display_translated_languages);
        translatedLanguageSpinner = findViewById(R.id.translatedLanguageSpinner);
        //Initializing the databaseManager
        databaseManager = new DatabaseManager(DisplayTranslatedLanguages.this);
        translatedPhrases = databaseManager.getAllTranslatedPhrases(); //Getting all the translatedPhrases from the database as an arrayList
        initializeTranslatedLanguagesSpinner();

        translatedLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String languageName = parent.getItemAtPosition(position).toString();
                setPhrasesRecyclerView(languageName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //Setting up bottom navigation in the activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_View);
        bottomNavigationView.setSelectedItemId(R.id.nav_translations);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        //When home icon is clicked navigates to Main activity
                        Intent intent = new Intent(DisplayTranslatedLanguages.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.nav_translations:
                        //When translation icon is clicked navigates to this activity
                        Intent intent2 = new Intent(DisplayTranslatedLanguages.this, DisplayTranslatedLanguages.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.nav_profile:
                        //When profile icon is clicked navigates to  ProfileActivity
                        Intent intent3 = new Intent(DisplayTranslatedLanguages.this, ProfileActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DisplayTranslatedLanguages.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //This method adds translatedLanguages to the spinner
    public void initializeTranslatedLanguagesSpinner(){
        translatedLanguages = databaseManager.getAllTranslatedLanguageNames(); //Getting all the translated languages from the spinner
        //Initializing an new ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DisplayTranslatedLanguages.this, android.R.layout.simple_spinner_item, translatedLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        translatedLanguageSpinner.setAdapter(adapter);
    }

    //This method takes the selected language from the spinner and and initialize the recyclerView with the translated phrase
    public void setPhrasesRecyclerView(String languageName){
        //Initializing a new RecyclerView
        RecyclerView recyclerView = findViewById(R.id.translatedPhraseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for(TranslatedPhrases translatedPhrase : translatedPhrases){
            if(translatedPhrase.getTranslatedLanguage().equals(languageName)){
                //Populating the recyclerView Adapter with translated foreign phrases
                mAdapter = new ForeignPhrasesRecyclerViewAdapter(this, translatedPhrase.getForeignPhrases());
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
