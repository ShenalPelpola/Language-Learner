package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.R;

public class AddPhraseActivity extends AppCompatActivity {
    private static final String IS_SUCCESS = "IS_SUCCESS";
    private static final String EMPTY_FIELDS = "EMPTY_FIELDS";
    private static final String NOT_UNIQUE = "NOT_UNIQUE";
    private EditText mPhraseEditText;
    private Button mPhraseSaveButton;
    protected DatabaseManager mDatabaseManager;
    private Dialog mDialog;
    private boolean isSuccess = false;
    private boolean isEmpty = false;
    private boolean isNotUnique = false;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // contains whether a certain dialog box is shown
        outState.putBoolean(IS_SUCCESS, isSuccess);
        outState.putBoolean(EMPTY_FIELDS, isEmpty);
        outState.putBoolean(NOT_UNIQUE, isNotUnique);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isSuccess = savedInstanceState.getBoolean(IS_SUCCESS);
        isEmpty = savedInstanceState.getBoolean(EMPTY_FIELDS);
        isNotUnique = savedInstanceState.getBoolean(NOT_UNIQUE);
        //if isSuccess is true success message dialog box is shown
        if(isSuccess){
            showSuccess();
        //if user hasn't entered a phrase
        }else if(isEmpty){
            showEmptyFieldsError();
        //if user enters an already available phrase
        }else if(isNotUnique){
            showNotUniqueError();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing the views, buttons and the dialog box
        setContentView(R.layout.activity_add_phrase);
        mPhraseEditText = findViewById(R.id.phraseEditText);
        mPhraseSaveButton = findViewById(R.id.phraseSaveButton);
        mDatabaseManager = new DatabaseManager(AddPhraseActivity.this);
        mDialog = new Dialog(this);
        //Disabling the dialog box closing when touching the window (Only enabling close by button option)
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        //When the save button is clicked addPhrase method is called
        mPhraseSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhrase();
            }
        });
    }


    private void addPhrase(){
        //Takes the user type input in the editTextView
        String userInputPhrase = mPhraseEditText.getText().toString().replaceAll(",", "").trim();
        if(userInputPhrase.length() > 0) {
            //Creating a new Phrase instance
            String randomColor = setPhraseColor();
            int imageResource = setImageResource();
            Phrase phrase = new Phrase(userInputPhrase, randomColor, imageResource);
            //Adding a new Phrase to the phrase table
            boolean isInserted = mDatabaseManager.insertNewPhrase(phrase);
            //If successfully inserted the showSuccess() is called
            if (isInserted) {
                showSuccess();
                isSuccess = true;
            } else {
                //else if not inserted the showNotUniqueError() is called
                showNotUniqueError();
                isNotUnique = true;
            }
         //if the edit text is empty
        }else{
            showEmptyFieldsError();
            isEmpty = true;
        }
    }


    //This method shows the success message dialog box
    public void showSuccess(){
        mDialog.setContentView(R.layout.pop_up_message);
        Button button_ok = mDialog.findViewById(R.id.ok_button);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSuccess = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void showNotUniqueError(){
        mDialog.setContentView(R.layout.pop_up_message_error);
        Button button_ok_error = mDialog.findViewById(R.id.ok_button_error);

        button_ok_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNotUnique = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    public void showEmptyFieldsError(){
        mDialog.setContentView(R.layout.pop_up_message_error);
        Button button_ok_error = mDialog.findViewById(R.id.ok_button_error);
        TextView error_texView = mDialog.findViewById(R.id.error_texView);
        error_texView.setText(R.string.fields_empty_error);

        button_ok_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmpty = false;
                mDialog.dismiss();
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    //Returns a random color from the colors array
    public String setPhraseColor(){
        Random random = new Random();
        List<String> colors = Arrays.asList("#FED58C", "#FC6E5B", "#FF9E5C",
                                            "#86D0AB","#7B7382","#8A9BED","#6FD9B1",
                                            "#FC7D86","#DAB4DB", "#77c89f", "#fb7c85",
                                            "#fedea5", "#9fb3df", "#e0bddd",
                                            "#9daaee", "#f16e1e", "#65a4d9",
                                            "#7fd2f2", "#d6a5d2", "#fc6d5c", "#f1ded0", "#f5a572", "#f5a572");
        return colors.get(random.nextInt(colors.size()));
    }


    //Returns a random image from the images array
    public int setImageResource(){
        Random random = new Random();
        List<String> images = Arrays.asList("pencil", "pencil2", "pencil3");
        String imageName = images.get(random.nextInt(images.size()));
        return getResources().getIdentifier(imageName,"drawable", "lk.shenal.languagelearner");
    }
}
