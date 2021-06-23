package com.example.text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    String NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.login);
        TextView register = findViewById(R.id.registerbo);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        sendhttprequest sendhttprequest = new sendhttprequest();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText)(findViewById(R.id.usernameedittext))).getText().toString();
                String password = ((EditText)(findViewById(R.id.mima))).getText().toString();
                String url = "https://www.wanandroid.com/user/login?username="+username+"&password="+password;
                Log.e(NAME,url);
                sendhttprequest.dorequest(url, "POST", new CallBackListener() {
                    @Override
                    public void onFinish(String out) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(out);
                            JSONObject data = jsonObject.optJSONObject("data");
                            int err = jsonObject.getInt("errorCode");
                            Log.e(NAME,""+err);
                            if(err == 0){
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
            }
        });
    }
}