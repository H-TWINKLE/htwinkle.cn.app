package cn.htwinkle.app.kit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

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
    public List<SmsPerson> getPhoneTelInfo(Context context) {
        List<SmsPerson> smsPeople = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM, NAME}, null, null, null);
        while (cursor.moveToNext()) {
            SmsPerson smsPerson = new SmsPerson();
            int nameIndex = cursor.getColumnIndex(NAME);
            if (nameIndex > -1) {
                smsPerson.setName(cursor.getString(nameIndex));
                smsPerson.setSendName(cursor.getString(nameIndex));
            }
            int numIndex = cursor.getColumnIndex(NUM);
            if (numIndex > -1) {
                smsPerson.setTelPhone(cursor.getString(numIndex));
            }
            if (!TextUtils.isEmpty(smsPerson.getName()) && !TextUtils.isEmpty(smsPerson.getTelPhone())) {
                smsPeople.add(smsPerson);
            }
        }
        return smsPeople;
    }
}
