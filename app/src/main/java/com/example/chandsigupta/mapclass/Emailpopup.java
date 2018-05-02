package com.example.chandsigupta.mapclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class Emailpopup extends AppCompatActivity {
    ArrayList<String> SampleArrayList ;
    ArrayAdapter<String> arrayAdapter;
    Pattern pattern;
    Account[] account ;
    String[] StringArray;
    ListView listView;
    private static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        pattern = Patterns.EMAIL_ADDRESS;
        SampleArrayList = new ArrayList<String>();
        View alertLayout = inflater.inflate(R.layout.custom_layout, null);
        listView = alertLayout.findViewById(R.id.listview1);
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        EnableRuntimePermission();
        GetAccountsName();
        alertdialog.setView(alertLayout);
        alertdialog.setCancelable(true);
        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"Over",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = alertdialog.create();
        dialog.show();
    }
    public void GetAccountsName(){
        try {
            account = AccountManager.get(Emailpopup.this).getAccounts();
        }
        catch (SecurityException e) {

        }

        for (Account TempAccount : account) {

            if (pattern.matcher(TempAccount.name).matches()) {

                SampleArrayList.add(TempAccount.name) ;
                Toast.makeText(Emailpopup.this,"Account is"+TempAccount.name +"Sample Arraylist size is"+SampleArrayList.size(),Toast.LENGTH_LONG).show();
            }

        }
        StringArray = SampleArrayList.toArray(new String[SampleArrayList.size()]);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, StringArray);
        listView.setAdapter(arrayAdapter);

    }

    public void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(Emailpopup.this, new String[]
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

                        Toast.makeText(Emailpopup.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Emailpopup.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

}
