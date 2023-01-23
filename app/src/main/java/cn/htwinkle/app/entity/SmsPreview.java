package cn.htwinkle.app.entity;

public class SmsPreview {
    private SmsPerson smsPerson;
    // 原生的文字
    private String originText;
    // 发送的文字
    private String sendText;
    // 发送的状态
    private Integer status = -999;

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    public static final int ERROR = -1;

    public SmsPreview() {
    }

    public SmsPreview(String originText, SmsPerson smsPerson) {
        this.smsPerson = smsPerson;
        this.originText = originText;
        this.sendText = smsPerson.getSendName() + "，" + originText;
    }

    public String getSendText() {
        return sendText;
    }

    public SmsPreview setSendText(String sendText) {
        this.sendText = sendText;
        return this;
    }

    public SmsPerson getSmsPerson() {
        return smsPerson;
    }

    public SmsPreview setSmsPerson(SmsPerson smsPerson) {
        this.smsPerson = smsPerson;
        return this;
    }

    public String getOriginText() {
        return originText;
    }

    public SmsPreview setOriginText(String originText) {
        this.originText = originText;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public SmsPreview setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
