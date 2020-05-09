package lk.shenal.languagelearner.Ui_Presenters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lk.shenal.languagelearner.DB.DatabaseManager;
import lk.shenal.languagelearner.Models.User;
import lk.shenal.languagelearner.R;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button submitButton;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        submitButton = findViewById(R.id.submitButton);
        databaseManager = new DatabaseManager(LoginActivity.this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    public void registerUser(){
        String userName = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(userName.length() > 0 && email.length() > 0 && password.length() > 0) {
            User user = new User(userName, email, password);
            boolean isInserted = databaseManager.insertNewUser(user);
            if (isInserted) {
                startMainActivity();
            } else {
                Toast.makeText(LoginActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void startMainActivity(){
        Intent homeActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(homeActivity);
        finish();
    }
}
