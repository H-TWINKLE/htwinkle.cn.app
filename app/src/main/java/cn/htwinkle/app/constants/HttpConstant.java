package cn.htwinkle.app.constants;

public interface HttpConstant {

    String GIT_HUB_RELEASE_LATEST = "https://api.github.com/repos/H-TWINKLE/htwinkle.cn.app/releases/latest";

    String HTTP_HTWINKLE_CN = "http://htwinkle.cn";
    String SMS_DELETE_BY = HTTP_HTWINKLE_CN + "/sms/deleteBy";
    String SMS_SAVE_OR_UPDATE = HTTP_HTWINKLE_CN + "/sms/saveOrUpdate";
    String GET_LIST_BY = HTTP_HTWINKLE_CN + "/sms/getBySn";
    String GET_WEL_PIC = HTTP_HTWINKLE_CN + "/picture/api";
    String FENGJING_RANDOM_PIC = HTTP_HTWINKLE_CN + "/picture/randomImg?plate=2&type=fengjing";

    String SRS_MAIN = "http://htwinkle.cn:8080/";

    String ONLINE_STREAM_LIST = "http://htwinkle.cn:1985/api/v1/streams/";
}
