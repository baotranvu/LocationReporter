package com.example.locationreporter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {

    public static int time;
    public final static int REQUEST_CODE=99;
    private EditText phone_edt;
    private EditText mail_edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        overridePendingTransition(R.anim.fragment_open_enter,R.anim.fragment_close_enter);
        Button btn = (Button) findViewById(R.id.button);
        phone_edt = (EditText) findViewById(R.id.editTextPhone);
        mail_edt = (EditText) findViewById(R.id.editTextTextEmailAddress);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);

            }

        });

        final Button save =(Button) findViewById(R.id.button2) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = new MainActivity();
                CheckConnection checkConnection = new CheckConnection(getApplicationContext());
                boolean check = checkConnection.isInternetConnected();
                if(check)
                {
                    getPhoneNum();
                    Intent intent = new Intent(SettingsActivity.this,MapsActivity.class);
                    startActivity(intent);
                    Intent map = new Intent(SettingsActivity.this,SMSService.class);
                    startService(map);

                }
                else
                {

                    Intent intent = new Intent(SettingsActivity.this,OfflineActivity.class);
                    startActivity(intent);
                }
                finish();



            }
        });
        List<String> list;
        Spinner spinner;
        list = new ArrayList<>();
        list.add("30 seconds");
        list.add("5 minutes");
        list.add("30 minutes");
        list.add("1 hour");
        spinner =(Spinner) findViewById(R.id.spinner3);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                switch (position)
                {
                    case 0:
                        time = (30*1000);
                        break;
                    case 1:
                        time = (300*1000);
                        break;
                    case 2:
                        time = (1800*1000);
                        break;
                    case 3:
                        time = (3600*1000);
                        break;

            }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                Config.phone_num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Config.phone_num=Config.phone_num.replaceAll("\\s+","");
                                phone_edt.setText(Config.phone_num);
                                
                            }
                        }
                    }
                    break;
                }
        }




    }
    public String getPhoneNum() {
        String temp;
        temp = String.valueOf(phone_edt.getText());
        if (!temp.equals("")) {
            Config.phone_num = temp;
            Toast toast = Toast.makeText(SettingsActivity.this, "Phone number added!", Toast.LENGTH_SHORT);
            toast.show();
            return Config.phone_num;
        } else {
            Toast toast = Toast.makeText(SettingsActivity.this, "Empty phone number!", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }
    public String getEmailAddress()
        {
            String temp;
            temp = String.valueOf(mail_edt.getText());
            if(!temp.equals("")) {
                Config.R_mail = temp;
                Toast toast = Toast.makeText(SettingsActivity.this, "E-mail address added!", Toast.LENGTH_SHORT);
                toast.show();
                return Config.R_mail;
            }
            else
            {
                Toast toast = Toast.makeText(SettingsActivity.this, "Empty e-mail anddress!", Toast.LENGTH_SHORT);
                toast.show();
                return null;
            }
        }
}









