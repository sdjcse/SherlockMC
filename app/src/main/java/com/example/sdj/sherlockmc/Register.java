package com.example.sdj.sherlockmc;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sdj.sherlockmc.beans.User;
import com.example.sdj.sherlockmc.restlayer.RegisterUser;
import com.example.sdj.sherlockmc.service.EncryptPassword;
import com.example.sdj.sherlockmc.service.UserEntryToDB;
import com.example.sdj.sherlockmc.utils.Constants;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {

    private Button register;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText primaryPhone;
    private EditText emergencyPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.register_name);
        email = (EditText) findViewById(R.id.register_emailId);
        password = (EditText) findViewById(R.id.register_password);
        primaryPhone = (EditText) findViewById(R.id.register_primaryPhone);
        emergencyPhone = (EditText) findViewById(R.id.register_emergencyPhone);
        register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String str_name = String.valueOf(name.getText());
                String str_password = String.valueOf(password.getText());
                String str_email = String.valueOf(email.getText());
                String str_primaryphone = String.valueOf(primaryPhone.getText());
                String str_emergencyphone = String.valueOf(emergencyPhone.getText());
                User user = null;
                boolean registerSuccess = false;

                if (str_name.equals("") || str_password.equals("") || str_email.equals("") || str_primaryphone.equals("") || str_emergencyphone.equals("")) {
                    Toast.makeText(getApplicationContext(),"All fields are mandatory!",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    user = new User(str_email, EncryptPassword.convertPasswordMD5(str_password),str_name,str_primaryphone,str_emergencyphone);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Log.d(null,"MD5 conversion failed");
                    return;
                }

                try {
                    registerSuccess = RegisterUser.registerUser(user);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(null,"user registration failed");
                    return;
                }
                if(registerSuccess)
                {
                    Log.d(null,"Registration Successful");
                    Toast.makeText(getApplicationContext(), Constants.REGISTRATION_SUCCESSFUL,Toast.LENGTH_LONG).show();
                    UserEntryToDB.insertUserToDB(openOrCreateDatabase(Constants.PHONE_PATH_FOLDER+Constants.SHERLOCK_DB_NAME,MODE_PRIVATE,null),user);
                    Intent intent = new Intent(Register.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), Constants.EMAIL_EXISTS,Toast.LENGTH_LONG).show();

            }


        });
    }

}
