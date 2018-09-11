package taha.com.whatsapp.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import taha.com.whatsapp.Adapters.CustomAdapter;
import taha.com.whatsapp.Models.Contact;
import taha.com.whatsapp.R;

public class MainActivity extends AppCompatActivity {
    // Identifier for the permission request
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    private ListView listContacts;
    private TextView txtMessage;

    private ArrayList contacts = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listContacts = (ListView)findViewById(R.id.listWhatsApp);
        txtMessage = (TextView)findViewById(R.id.txtMessage);

        //Check if we need to verify contacts permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            getPermissionToReadUserContacts();
        else{
            CustomAdapter adapter = new CustomAdapter(loadContacts(), getApplicationContext());
            adapter.notifyDataSetChanged();
            listContacts.setAdapter(adapter);
        }
    }

    private ArrayList<Contact> loadContacts() {
       ArrayList<Contact> contactList = new ArrayList();
        final String[] projection={
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.MIMETYPE,
                "account_type",
                ContactsContract.Data.DATA1,
                ContactsContract.Data.PHOTO_URI,
        };
        final String selection= ContactsContract.Data.MIMETYPE+" =? and account_type=?";
        final String[] selectionArgs = {
                "vnd.android.cursor.item/vnd.com.whatsapp.profile",
                "com.whatsapp"
        };
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        while(c.moveToNext()){
            String id=c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            String numberW=c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
            String[] parts = numberW.split("@");
            String numberPhone = parts[0];
            String number = "Tel : + " + numberPhone.substring(0, 3) + " " + numberPhone.substring(3, numberPhone.length());
            String image_uri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

            String name="";
            Cursor mCursor=getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                    ContactsContract.Contacts._ID+" =?",
                    new String[]{id},
                    null);
            while(mCursor.moveToNext()){
                name=mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            mCursor.close();
            contactList.add(new Contact(name,number,image_uri));

        }

       return contactList;
    }

    public void getPermissionToReadUserContacts() {
            // This will show the standard permission request dialog UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                CustomAdapter adapter = new CustomAdapter(loadContacts(), getApplicationContext());
                listContacts.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
