package com.example.text;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
public class RegisterActivity extends AppCompatActivity {
    String NAME = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button button = findViewById(R.id.registerbo);
        sendhttprequest sendhttprequest = new sendhttprequest();
        button.setOnClickListener(v -> {
            String username = ((EditText) (findViewById(R.id.ed1))).getText().toString();
            String password = ((EditText) (findViewById(R.id.ed2))).getText().toString();
            String repassword = ((EditText) (findViewById(R.id.ed3))).getText().toString();
            String url = "https://www.wanandroid.com/user/login?username=" + username + "&password=" + password + "&repassword=" + repassword;
            Log.e(NAME, url);
            sendhttprequest.dorequest(url, "POST", new CallBackListener() {
                @Override
                public void onFinish(String out) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(out);
                        JSONObject data = jsonObject.optJSONObject("data");
                        int err = jsonObject.getInt("errorCode");
                        String errmsg = jsonObject.getString("errorMsg");
                        Log.e(NAME, "" + err);
                        if (err == 0) {
                            RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show());
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, errmsg, Toast.LENGTH_SHORT).show());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception ex) {
                }
            });
        });
    }
}
