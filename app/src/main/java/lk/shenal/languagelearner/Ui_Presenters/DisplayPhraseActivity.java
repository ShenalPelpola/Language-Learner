package lk.shenal.languagelearner.Ui_Presenters;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Helper_adapters.RecyclerViewAdapter;
import lk.shenal.languagelearner.Models.Phrase;
import lk.shenal.languagelearner.R;

public class DisplayPhraseActivity extends AppCompatActivity {
    private RecyclerViewAdapter mAdapter;
    protected DatabaseManager databaseManager;
    private EditText searchPhraseEditText;
    private List<Phrase> phrases;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrase);
        //initializing views
        searchPhraseEditText = findViewById(R.id.searchPhraseEditText);
        //initializing databases manager context is set to this activity
        databaseManager = new DatabaseManager(DisplayPhraseActivity.this);
        phrases = databaseManager.getAllPhrases();//getting saved phrase from the db as an arrayList

        //Initializing and declaring the recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setting up recyclerview adapter
        mAdapter = new RecyclerViewAdapter(this, phrases);
        recyclerView.setAdapter(mAdapter);


        searchPhraseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //notifies after edit text is change
            @Override
            public void afterTextChanged(Editable s) {
                filterPhrases(s.toString());
            }
        });

    }

    //This method takes the test from the edit text as a argument
    private void filterPhrases(String text) {
        ArrayList<Phrase> filteredPhrase = new ArrayList<>();
        //loops though the entire phrases array and check if entered phrase is available
        for(Phrase phrase : phrases){
            if(phrase.getPhraseContent().toLowerCase().contains(text.toLowerCase())){
                filteredPhrase.add(phrase);
            }
        }
        mAdapter.filterList(filteredPhrase);
    }
}
