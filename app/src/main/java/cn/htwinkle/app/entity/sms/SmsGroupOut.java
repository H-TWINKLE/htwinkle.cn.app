package cn.htwinkle.app.entity.sms;

import android.content.Context;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

import cn.htwinkle.app.entity.SmsPerson;
import cn.htwinkle.app.kit.PhoneKit;

public class SmsGroupOut implements Serializable {

    /**
     * sms_backup : 1
     * sms_name : 张俊dddddd
     * sms_tel : 13038132020
     * sms_send_status : 1
     * tel_sn : 12121212
     * sms_send_name : 张大俊
     */

    @JSONField(name = "sms_backup")
    private Integer smsBackup;

    @JSONField(name = "sms_name")
    private String smsName;

    @JSONField(name = "sms_tel")
    private String smsTel;

    @JSONField(name = "sms_send_status")
    private Integer smsSendStatus;

    @JSONField(name = "tel_sn")
    private String telSn;

    @JSONField(name = "sms_send_name")
    private String smsSendName;

    public SmsGroupOut() {
    }

    public SmsGroupOut(SmsPerson smsPerson, Context context) {
        setSmsBackup(smsPerson.isBackUp() ? 1 : 0);
        setSmsName(smsPerson.getName());
        setSmsSendName(smsPerson.getSendName());
        setSmsTel(smsPerson.getTelPhone());
        setSmsSendStatus(smsPerson.isChecked() ? 1 : 0);
        setTelSn(PhoneKit.INSTANCE.getDeviceId(context));
    }

    public SmsPerson of() {
        SmsPerson smsPerson = new SmsPerson();
        smsPerson.setBackUp(getSmsBackup() == 1);
        smsPerson.setSendName(getSmsSendName());
        smsPerson.setChecked(getSmsSendStatus() == 1);
        smsPerson.setName(getSmsName());
        smsPerson.setTelPhone(getSmsTel());
        return smsPerson;
    }

    public Integer getSmsBackup() {
        return smsBackup;
    }

    public void setSmsBackup(Integer smsBackup) {
        this.smsBackup = smsBackup;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName;
    }

    public String getSmsTel() {
        return smsTel;
    }

    public void setSmsTel(String smsTel) {
        this.smsTel = smsTel;
    }

    public Integer getSmsSendStatus() {
        return smsSendStatus;
    }

    public void setSmsSendStatus(Integer smsSendStatus) {
        this.smsSendStatus = smsSendStatus;
    }

    public String getTelSn() {
        return telSn;
    }

    public void setTelSn(String telSn) {
        this.telSn = telSn;
    }

    public String getSmsSendName() {
        return smsSendName;
    }

    public void setSmsSendName(String smsSendName) {
        this.smsSendName = smsSendName;
    }
}
