package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = findViewById(R.id.Username);
        TextView password = findViewById(R.id.Password);

        Button loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    AssetManager assetManager = getAssets();
                    InputStream inputStream = assetManager.open("log_pass.json");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    JSONArray usersArray = new JSONArray(stringBuilder.toString());

                    if (checkAuthorization(usersArray, enteredUsername, enteredPassword)) {
                        Toast.makeText(MainActivity.this, "Authorization success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, activity_registration.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Authorization FAILED1", Toast.LENGTH_SHORT).show();
                    }

                    inputStream.close();
                } catch (IOException | JSONException e) {
                    Toast.makeText(MainActivity.this, "Authorization FAILED2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkAuthorization(JSONArray usersArray, String enteredUsername, String enteredPassword) throws JSONException {
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            String savedUsername = user.getString("username");
            String savedPassword = user.getString("password");
            if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                return true;
            }
        }
        return false;
    }
}
