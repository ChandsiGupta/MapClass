package com.example.chandsigupta.mapclass;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class EmailActivity extends AppCompatActivity {
    ListView listView ;
    Button button;
    ArrayList<String> SampleArrayList ;
    ArrayAdapter<String> arrayAdapter;
    Pattern pattern;
    Account[] account ;
    String[] StringArray;
private static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        pattern = Patterns.EMAIL_ADDRESS;
        SampleArrayList = new ArrayList<String>();
        Button fetchEmails = findViewById(R.id.emailact);
        ListView listView1 = findViewById(R.id.listview1);

        fetchEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableRuntimePermission();
                GetAccountsName();

                //Setting up string array into array adapter.
                listView1.setAdapter(arrayAdapter);
            }
        });
    }
    public void GetAccountsName(){
        try {
            account = AccountManager.get(EmailActivity.this).getAccounts();
        }
        catch (SecurityException e) {

        }

        for (Account TempAccount : account) {

            if (pattern.matcher(TempAccount.name).matches()) {

                SampleArrayList.add(TempAccount.name) ;
            }

        }
        StringArray = SampleArrayList.toArray(new String[SampleArrayList.size()]);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, StringArray);


    }

    public void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(EmailActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean GetAccountPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (GetAccountPermission && ReadPhoneStatePermission) {

                        Toast.makeText(EmailActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(EmailActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

}
