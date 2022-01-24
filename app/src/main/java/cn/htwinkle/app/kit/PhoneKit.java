package cn.htwinkle.app.kit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import cn.htwinkle.app.entity.SmsPerson;

public enum PhoneKit {
    INSTANCE;
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    //获取所有联系人
    public List<SmsPerson> getPhone(Context context) {
        List<SmsPerson> smsPeople = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM, NAME}, null, null, null);
        while (cursor.moveToNext()) {
            SmsPerson smsPerson = new SmsPerson();
            int nameI = cursor.getColumnIndex(NAME);
            if (nameI > -1) {
                smsPerson.setName(cursor.getString(nameI));
            }
            int numI = cursor.getColumnIndex(NUM);
            if (numI > -1) {
                smsPerson.setTelPhone(cursor.getString(numI));
            }
            smsPeople.add(smsPerson);
        }
        return smsPeople;
    }
}
