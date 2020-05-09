package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Objects;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Helper_adapters.LanguageListAdapter;
import lk.shenal.languagelearner.Models.Language;
import lk.shenal.languagelearner.R;


public class LanguageSubscriptionActivity extends AppCompatActivity {
    private static final String TAG = "UPDATE_TAG";
    private static final String IS_SUBSCRIBED = "IS_SUBSCRIBED";
    private boolean isSubscribed = false;
    private DatabaseManager databaseManager;
    private Button updateLanguagesButton;
    private ListView mLanguageListView;
    private LanguageListAdapter languageListAdapter;
    private Dialog mDialog;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SUBSCRIBED, isSubscribed);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isSubscribed = savedInstanceState.getBoolean(IS_SUBSCRIBED);
        if(isSubscribed) {
            showSuccess();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);
        databaseManager = new DatabaseManager(LanguageSubscriptionActivity.this);
        updateLanguagesButton = findViewById(R.id.updateLanguagesButton);

        //Initializing the listView
        mLanguageListView = findViewById(R.id.languagesListView);
        //Initializing the listView Adapter with context and all languages from the languages table
        languageListAdapter = new LanguageListAdapter(this, databaseManager.getLanguages());
        mLanguageListView.setAdapter(languageListAdapter);

        mDialog = new Dialog(this);
        //Disabling the dialog box closing when touching the window
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);


        updateLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Language, Boolean> checkedLanguages = languageListAdapter.getCheckedLanguages(); //Getting the language and isChecked as a map
                for(Map.Entry mapElement : checkedLanguages.entrySet()){ //looping the values in the map
                    Language language = (Language) mapElement.getKey();
                    String languageName = language.getLanguageName();
                    Boolean isChecked = (Boolean) mapElement.getValue();
                    boolean isUpdate = databaseManager.updateLanguages(languageName,isChecked); //Updating the isChecked in languages table
                    if(isUpdate){
                        //Refreshing the listView adapter with updates languages
                        languageListAdapter = new LanguageListAdapter(LanguageSubscriptionActivity.this, databaseManager.getLanguages());
                        mLanguageListView.setAdapter(languageListAdapter);
                        isSubscribed = true;
                        showSuccess();
                    }else{
                        Log.e(TAG, "update failed");
                    }
                }
            }
        });
    }



    public void showSuccess(){
        mDialog.setContentView(R.layout.pop_up_message);

        Button button_ok = mDialog.findViewById(R.id.ok_button);
        TextView translatedMessageTextView = mDialog.findViewById(R.id.translatedMessageTextView);
        translatedMessageTextView.setText("Congratulations your subscribed languages have been changed!");
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSubscribed = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

}
