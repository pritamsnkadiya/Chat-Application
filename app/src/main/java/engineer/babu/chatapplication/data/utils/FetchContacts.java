package engineer.babu.chatapplication.data.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import engineer.babu.chatapplication.data.database.AppDatabase;
import engineer.babu.chatapplication.data.database.AppExecutors;
import engineer.babu.chatapplication.data.model.Person;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FetchContacts extends AsyncTask<Void, Void, List> {
    private Context activity;
    private OnContactFetchListener listener;
    private AppDatabase mDb;

    public FetchContacts(Context context, OnContactFetchListener listener) {
        activity = context;
        this.listener = listener;
        mDb = AppDatabase.getInstance(context);
    }

    @Override
    protected List doInBackground(Void... voids) {
        List<Person> contactList = new ArrayList<>();
        // get Contacts here

        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Person contact = new Person();
                        contact.setName(name);
                        contact.setNumber(phoneNo);
                        AppExecutors.getInstance().diskIO().execute(() -> mDb.personDao().insertPerson(contact));
                        contactList.add(contact);

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return contactList;
    }

    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        if (listener != null) {
            listener.onContactFetch(list);
        }
    }

    public interface OnContactFetchListener {
        void onContactFetch(List list);
    }
}