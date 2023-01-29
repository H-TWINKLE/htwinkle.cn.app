package cn.htwinkle.app.kit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.SmsPerson;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

public enum PhoneKit {
    INSTANCE;

    private static final String TAG = "PhoneKit";

    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    public static final String DEVICE_ID_FILE = Constants.APP_PACKAGE_NAME + ".pid";

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

    public String getDeviceId(Context context) {
        String androidId = CommKit.safety(this::getSdCardDeviceId, true, null);
        if (StrUtil.isEmpty(androidId)) {
            androidId = getAndroidId(context);
        }
        if (StrUtil.isEmpty(androidId)) {
            androidId = "";
        }
        Log.i(TAG, "getDeviceId: " + androidId);
        return androidId;
    }

    private String getAndroidId(Context context) {
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (StrUtil.isNotEmpty(string)) {
                return string;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public String getSdCardDeviceId() {
        File savedFile = getSavedFile(DEVICE_ID_FILE);
        if (savedFile == null) {
            String content = UUID.fastUUID().toString(true);
            File file = new File(getSdCardParentDir(), DEVICE_ID_FILE);
            FileUtil.writeUtf8String(content, file);
            return null;
        }
        List<String> strings = FileUtil.readUtf8Lines(savedFile);
        return StrUtil.join("", strings);
    }

    public static File getSavedFile(String fileName) {
        File parent = getSdCardParentDir();
        File realFile = new File(parent, fileName);
        if (realFile.exists()) {
            return realFile;
        }
        return null;
    }

    public static File getSdCardParentDir() {
        File parentDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Constants.APP_PACKAGE_NAME);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        return parentDir;
    }

    public static File saveFileToSdCard(String fileName, File file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File parent = getSdCardParentDir();
            File realFile = new File(parent, fileName);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileOutputStream fileOutputStream = new FileOutputStream(realFile);
                byte[] buffer = new byte[1024];
                int byteRead;
                while (-1 != (byteRead = fileInputStream.read(buffer))) {
                    fileOutputStream.write(buffer, 0, byteRead);
                }
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                return realFile;
            } catch (IOException e) {
                Log.e(TAG, "saveFileToSdCard: 复制文件失败: " + e.getLocalizedMessage());
            }
        }
        return null;
    }


}
