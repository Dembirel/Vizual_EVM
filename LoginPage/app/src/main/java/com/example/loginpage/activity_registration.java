package com.example.loginpage;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class activity_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.Username);
        TextView password = (TextView) findViewById(R.id.Password);

        Button regButton = findViewById(R.id.loginbtn);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                JSONObject newUser = new JSONObject();
                try {
                    newUser.put("username", enteredUsername);
                    newUser.put("password", enteredPassword);

                    FileInputStream fileInputStream = openFileInput("users.json");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    JSONArray usersArray = new JSONArray(stringBuilder.toString());
                    usersArray.put(newUser);
                    fileInputStream.close();

                    FileWriter file = new FileWriter(getFilesDir() + "/users.json");
                    file.write(usersArray.toString());
                    file.flush();
                    file.close();
                } catch (FileNotFoundException e) {
                    JSONArray usersArray = new JSONArray();
                    usersArray.put(newUser);
                    try {
                        FileWriter file = new FileWriter(getFilesDir() + "/users.json");
                        file.write(usersArray.toString());
                        file.flush();
                        file.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goBack(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}