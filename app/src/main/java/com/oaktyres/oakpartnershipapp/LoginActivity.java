package com.oaktyres.oakpartnershipapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public EditText user1;
    public EditText key1;
    public TextView error;

    Button forgotpassword;
    Button login;

    public ImageView imguser;
    public ImageView imgkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user1 = (EditText) findViewById(R.id.User);
        key1 = (EditText) findViewById(R.id.Key);
        forgotpassword = findViewById(R.id.forgotpassword);
        login = findViewById(R.id.login);

        imguser = findViewById(R.id.ivEmail);
        imgkey = findViewById(R.id.ivKEY);

        buttonpressed();

    }

    public void storedetails() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save details?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void buttonpressed() {

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    storedetails();

                    if (checkdetails() == true) {

                        Intent login = new Intent(LoginActivity.this, MainActivity.class);

                        Log.i("USER1 is: ", user1.getText().toString());
                        String user = user1.getText().toString();
                        String key = key1.getText().toString();

                        login.putExtra("KEY_USER", user);
                        login.putExtra("KEY_PASSWORD", key);
                        startActivity(login);
                    }
                }
            });

            forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = "https://oak-partnership.co.uk/Account/ForgotPassword";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
    }

    private boolean checkdetails() {

        String etUsername = user1.getText().toString();
        String etKey = key1.getText().toString();
        error = (TextView) findViewById(R.id.tverror);

        if (etUsername.matches("") || etKey.matches("")) {
            Log.v("Login Attempt", "Details are missing");
            error.setText("Both fields are required.");
            error.setVisibility(View.VISIBLE);

            if (etKey.matches("")) {
                vibrator();
                imgkey.setVisibility(View.VISIBLE);
            }

            if (etUsername.matches("")){
                vibrator();
                imguser.setVisibility(View.VISIBLE);
            }

            if (!etKey.matches("")) {
                imgkey.setVisibility(View.INVISIBLE);
            }

            if (!etUsername.matches("")) {
                imguser.setVisibility(View.INVISIBLE);
            }
            return false;
        }

        else {
            return true;
        }
    }

    private void vibrator() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 50 milliseconds
        v.vibrate(50);
    }
}
