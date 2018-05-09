package com.example.facebookexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    LoginButton login;
    TextView text;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=findViewById(R.id.login);
        text=findViewById(R.id.text);
        login.setReadPermissions(Arrays.asList(EMAIL));
        callbackManager=CallbackManager.Factory.create();

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if(AccessToken.getCurrentAccessToken()!=null){
                    RequestData();

                }



            }

            @Override
            public void onCancel() {
              text.setText("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void RequestData() {

        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
              JSONObject jsonObject=response.getJSONObject();
              if(jsonObject!=null){
                  try {
                      String txt="<b>Name:</b>"+""+jsonObject.getString("name")+
                              "<br><br><b>Link:</b>"+jsonObject.getString("email");
                      text.setText(Html.fromHtml(txt));
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
