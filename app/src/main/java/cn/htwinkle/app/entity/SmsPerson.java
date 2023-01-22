package cn.htwinkle.app.entity;

import android.util.Log;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import cn.htwinkle.app.kit.DbKit;

@Table(name = "SMS_PERSON")
public class SmsPerson {

    private static final String TAG = "SmsPerson";

    @Column(name = "name")
    private String name;

    @Column(name = "telPhone", isId = true, autoGen = false, property = "NOT NULL")
    private String telPhone;

    @Column(name = "sendName")
    private String sendName;

    @Column(name = "checked")
    private boolean checked;

    @Column(name = "backUp")
    private boolean backUp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public boolean isBackUp() {
        return backUp;
    }

    public void setBackUp(boolean backUp) {
        this.backUp = backUp;
    }

    public void saveSelfSafety() {
        try {
            DbKit.INSTANCE.getDb().saveOrUpdate(this);
        } catch (DbException e) {
            Log.e(TAG, "saveSelfSafety: 缓存联系人失败：" + e.getLocalizedMessage());
        }
    }

    public void deleteSelf() {
        try {
            DbKit.INSTANCE.getDb().delete(this);
        } catch (DbException e) {
            Log.e(TAG, "saveSelfSafety: 删除缓存联系人失败：" + e.getLocalizedMessage());
        }
    }
}
