package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.shenal.languagelearner.R;

public class MainActivity extends AppCompatActivity {
    private Button mAddPhraseButton;
    private Button mDisplayPhrasesButton;
    private Button mEditPhraseButton;
    private Button mLanguageSubscriptionButton;
    private Button mTranslateButton;
    private Button mQuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddPhraseButton = findViewById(R.id.addPhraseButton);
        mDisplayPhrasesButton = findViewById(R.id.displayPhrasesButton);
        mEditPhraseButton = findViewById(R.id.editPhrasesButton);
        mLanguageSubscriptionButton = findViewById(R.id.languageSubscriptionButton);
        mTranslateButton = findViewById(R.id.translateButton);
        mQuitButton = findViewById(R.id.quit_button);



        mAddPhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddPhraseActivity();
            }
        });

        mDisplayPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDisplayPhraseActivity();
            }
        });

        mEditPhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditPhraseActivity();
            }
        });

        mLanguageSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySubscriptionActivity();
            }
        });

        mTranslateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTranslateActivity();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_View);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_translations:
                        Intent intent2 = new Intent(MainActivity.this, DisplayTranslatedLanguages.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
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
        finish();
    }


    private void displayAddPhraseActivity() {
        Intent intent = new Intent(this, AddPhraseActivity.class);
        startActivity(intent);
    }

    private void displayDisplayPhraseActivity() {
        Intent intent = new Intent(this, DisplayPhraseActivity.class);
        startActivity(intent);
    }

    private void displayEditPhraseActivity() {
        Intent intent = new Intent(this, EditPhraseActivity.class);
        startActivity(intent);
    }

    private void displaySubscriptionActivity() {
        Intent intent = new Intent(this, LanguageSubscriptionActivity.class);
        startActivity(intent);
    }


    private void displayTranslateActivity() {
        Intent intent = new Intent(this, TranslateActivity.class);
        startActivity(intent);
    }
}
