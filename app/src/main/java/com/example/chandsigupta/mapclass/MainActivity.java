package com.example.chandsigupta.mapclass;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;


public class MainActivity extends Activity {

    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    private Button tBMobileData, data, image_picker, next, email, secure;
    Account[] account;
    Pattern pattern;
    private static final int RequestPermissionCode = 1;
    ArrayList<String> SampleArrayList;
    private final String TAG = "MainActivity";
    TextView package_name;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().getDecorView().setSystemUiVisibility(flags);
        tBMobileData = findViewById(R.id.tBMobileData);
        data = findViewById(R.id.data);
        image_picker = findViewById(R.id.image_picker);
        package_name = findViewById(R.id.package_name);
        next = findViewById(R.id.next);
        email = findViewById(R.id.email);
        secure = findViewById(R.id.secure);
        secure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDeviceSecured();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });
        PrefUtils.setKioskModeActive(true, getApplicationContext());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Emailpopup.class);
                startActivity(intent);
            }
        });
        tBMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                startActivity(intent);
            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Email is", Toast.LENGTH_LONG).show();
                PackageManager localPackageManager = getPackageManager();
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                String str = localPackageManager.resolveActivity(intent,
                        PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
                Log.e("Current launcher:", str);
            }
        });
        image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isDeviceSecured() {
        String LOCKSCREEN_UTILS = "com.android.internal.widget.LockPatternUtils";
        try {
            Class<?> lockUtilsClass = Class.forName(LOCKSCREEN_UTILS);
            // "this" is a Context, in my case an Activity
            Object lockUtils = lockUtilsClass.getConstructor(Context.class).newInstance(this);

            Method method = lockUtilsClass.getMethod("getActivePasswordQuality");

            int lockProtectionLevel = (Integer) method.invoke(lockUtils); // Thank you esme_louise for the cast hint

           if(lockProtectionLevel >=DevicePolicyManager.PASSWORD_QUALITY_SOMETHING)
           {
               Log.e(TAG,"Locked with PASSWORD_QUALITY_SOMETHING or pattern");

           }
            else if (lockProtectionLevel >= DevicePolicyManager.PASSWORD_QUALITY_NUMERIC) {
                Log.e(TAG,"Locked with PASSWORD_QUALITY_NUMERIC");
                return true;
            }
            else if(lockProtectionLevel>=DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC)
           {
               Log.e(TAG,"Locked with PASSWORD_QUALITY_ALPHABETIC");

           }
        } catch (Exception e) {
            Log.e("reflectInternalUtils", "ex:" + e);
        }
        Log.e(TAG,"Not Locked with PASSWORD_QUALITY_NUMERIC");

        return false;
    }

}

