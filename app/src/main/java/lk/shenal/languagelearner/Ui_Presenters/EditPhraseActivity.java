package lk.shenal.languagelearner.Ui_Presenters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.R;

public class EditPhraseActivity extends AppCompatActivity {
    private static final String IS_EDIT_CLICKED = "IS_EDIT_CLICKED";
    private static final String IS_INITIALIZED = "IS_INITIALIZED";
    private DatabaseManager databaseManager;
    private EditText updateEditText;
    private Button updatePhraseButton;
    private LinearLayout parent;
    private RadioButton radioButtonView;
    private RadioGroup radioGroup;
    private Button savePhraseButton;
    private Dialog dialog;
    private int checkedResultButtonsId;



//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(IS_INITIALIZED, isInitialized);
//        outState.putBoolean(IS_EDIT_CLICKED, isEditButtonClicked);
//        outState.putInt("radGroup1", radioGroup.getCheckedRadioButtonId());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        isEditButtonClicked = savedInstanceState.getBoolean(IS_EDIT_CLICKED);
//        checkedResultButtonsId = savedInstanceState.getInt("radGroup1");
//        isInitialized = savedInstanceState.getBoolean(IS_INITIALIZED);
//        if(isEditButtonClicked){
//            getSelectRadioValue();
//            changeSelectedRadioButton();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrase);
        updateEditText = findViewById(R.id.updateEditText);
        updatePhraseButton = findViewById(R.id.updatePhraseButton);
        savePhraseButton = findViewById(R.id.savePhraseButton);
        dialog = new Dialog(this);

        databaseManager = new DatabaseManager(EditPhraseActivity.this);



        initializePhraseList();

        updatePhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectRadioValue();
                changeSelectedRadioButton();
            }
        });


        savePhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void initializePhraseList(){
        parent = findViewById(R.id.radio_parent);
        radioGroup = new RadioGroup(this);
        ArrayList<Phrase> phrases = databaseManager.getAllPhrases();
        for(Phrase phrase: phrases){
            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setPadding(10, 50, 40, 50);
            radioButtonView.setText(phrase.getPhraseContent());
            radioButtonView.setTextSize(16);
            radioButtonView.setTextColor(Color.parseColor("#5a6174"));
            radioGroup.addView(radioButtonView);
        }
        parent.addView(radioGroup);
    }

    public void getSelectRadioValue(){
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            showEmptyFieldsError();
        }else{
            checkedResultButtonsId = radioGroup.getCheckedRadioButtonId();
            radioButtonView = findViewById(checkedResultButtonsId);
            updateEditText.setText(radioButtonView.getText());
        }
    }

    public void changeSelectedRadioButton(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonView = findViewById(checkedId);
                updateEditText.setText(radioButtonView.getText());
            }
        });
    }

    public void update(){
        if (updateEditText.getText().toString().equals("")) {
            showEmptyFieldsError();
        }else{
            String phraseContent = radioButtonView.getText().toString();
            String updatePhraseContent = updateEditText.getText().toString().replaceAll(",", "").trim();;

            boolean isUpdate = databaseManager.updatePhrase(phraseContent, updatePhraseContent);
            if(isUpdate){
                parent.removeAllViews();
                initializePhraseList();
                showSuccess();
            }else{
                showPhraseAvailableError();
            }
        }
        updateEditText.setText("");
    }

    public void showSuccess(){
        dialog.setContentView(R.layout.pop_up_message);
        Button button_ok = dialog.findViewById(R.id.ok_button);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showEmptyFieldsError(){
        dialog.setContentView(R.layout.pop_up_message_error);
        Button button_ok_error = dialog.findViewById(R.id.ok_button_error);
        TextView error_texView = dialog.findViewById(R.id.error_texView);
        error_texView.setText("Enter what you want your new phrase to be");

        button_ok_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showPhraseAvailableError() {
        dialog.setContentView(R.layout.pop_up_message_error);
        Button button_ok_error = dialog.findViewById(R.id.ok_button_error);
        TextView error_texView = dialog.findViewById(R.id.error_texView);
        error_texView.setText("Phrase is already available");

        button_ok_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
