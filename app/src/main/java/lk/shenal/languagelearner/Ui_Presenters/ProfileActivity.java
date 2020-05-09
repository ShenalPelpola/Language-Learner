package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Models.TranslatedPhrases;
import lk.shenal.languagelearner.Models.User;
import lk.shenal.languagelearner.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private TextView emailTextView;
    private TextView subscribedLanguages;
    private TextView totalTranslationsTextView;
    private TextView numberOfPhrasesTextView;
    private Button quitButton;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Initializing textViews
        userNameTextView = findViewById(R.id.userNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        subscribedLanguages = findViewById(R.id.numberOfLanguages);
        totalTranslationsTextView = findViewById(R.id.totalTranslationsTextView);
        numberOfPhrasesTextView = findViewById(R.id.numberOfPhrases);
        quitButton = findViewById(R.id.quit_button);
        databaseManager = new DatabaseManager(this);

       setBasicUserInformation();
       setUserStats();

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitApp();
            }
        });

       //Setting up the bottom navigation in the profileActivity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_View);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.nav_translations:
                        Intent intent2 = new Intent(ProfileActivity.this, DisplayTranslatedLanguages.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(ProfileActivity.this, ProfileActivity.class);
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
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Setting the basic user information to textViews
    public void setBasicUserInformation(){
        User user = databaseManager.getUser();
        userNameTextView.setText(user.getUserName());
        emailTextView.setText(user.getEmail());
    }

    public void setUserStats(){
        int totalTranslations = 0;
        int noOfSubscribedLanguages = databaseManager.getSubscribedLanguageNames().size();
        int totalPhrases = databaseManager.getAllPhrases().size();
        numberOfPhrasesTextView.setText(Integer.toString(totalPhrases));
        subscribedLanguages.setText(Integer.toString(noOfSubscribedLanguages));

        ArrayList<TranslatedPhrases> translatedPhrases = databaseManager.getAllTranslatedPhrases();
        for(TranslatedPhrases translatedPhrase : translatedPhrases){
            totalTranslations += translatedPhrase.getForeignPhrases().size();
        }
        totalTranslationsTextView.setText(Integer.toString(totalTranslations));

    }

    public void quitApp(){
        finish();
        System.exit(0);
    }
}
