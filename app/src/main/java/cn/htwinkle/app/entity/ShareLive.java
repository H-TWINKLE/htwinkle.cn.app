package cn.htwinkle.app.entity;

/**
 * 正在分享的用户
 */
public class ShareLive {
    /**
     * 数据id
     */
    private String id;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备名字
     */
    private String deviceName;
    /**
     * 直播地址
     */
    private String url;

    public String getId() {
        return id;
    }

    public ShareLive setId(String id) {
        this.id = id;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public ShareLive setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public ShareLive setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ShareLive setUrl(String url) {
        this.url = url;
        return this;
    }
}
