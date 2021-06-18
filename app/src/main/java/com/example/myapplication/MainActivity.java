package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private EditText email, password;
    private Button submit;
    private String error;
    private Retrofit retrofit;
    private API api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://cinema.areas.su/").build();
        api = retrofit.create(API.class);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func();
            }
        });
    }
    public void Dilog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Ошибка")
                .setMessage(error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });
        builder.create().show();
    }
    private boolean emailAuth(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void func()
    {
        if(email.getText().toString().equals("") || password.getText().toString().equals(""))
        {
            error = "Не все поля введены";
            Dilog(MainActivity.this);
        }
        else if(!emailAuth(email.getText().toString()))
        {
            error = "Некорректная почта";
            Dilog(MainActivity.this);
        }
        else
        {
            param par = new param();
            par.setEmail(email.getText().toString());
            par.setPassword(password.getText().toString());
            Call<param> call = api.poster(par);
            call.enqueue(new Callback<param>() {
                @Override
                public void onResponse(Call<param> call, Response<param> response) {
                    if(response.isSuccessful())
                    {
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Успешный вход", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        error = "Не правильная почта или пароль";
                        Dilog(MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<param> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}