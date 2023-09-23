package com.th7.easynetlogin;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String password = "pwd";
    public static String username = "user";

    EditText usrEdit;
    EditText passEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        String user = DataUtils.ReadStringValue(this,username,"");
        String pwd = DataUtils.ReadStringValue(this,password,"");
        usrEdit = findViewById(R.id.editTextText);
        passEdit = findViewById(R.id.editTextTextPassword);
        usrEdit.setText(user);
        passEdit.setText(pwd);
    }

    private static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public void OnClick(View view) {
        try {
            if (view.getId() == R.id.button) {
                try {
                    DataUtils.saveStringValue(this, username, usrEdit.getText().toString());
                    DataUtils.saveStringValue(this, password, passEdit.getText().toString());
                } catch (Exception ex) {
                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.button2) {
                if (!isWifi(this)) {
                    Toast.makeText(this, "请连接至WiFi或关闭移动数据", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    String ip = LoginUtils.getLocalIPAddress();
                    //Toast.makeText(this, ip, Toast.LENGTH_SHORT).show();
                    String res = LoginUtils.login(usrEdit.getText().toString(), passEdit.getText().toString(), ip);
                    //Log.e("OPT:", res);
                    if (res.contains("认证成功") || res.contains("已经在线")){
                        Toast.makeText(this, "登录成功 局域网IP: "+ip, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "登录失败 "+ex, Toast.LENGTH_LONG).show();
                    Log.e("OPT:", String.valueOf(ex));
                }
            }
        }
        catch (Exception ignored) {}
    }
}