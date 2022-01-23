package cn.htwinkle.app.kit;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import cn.htwinkle.app.constants.Constants;

public class StrKit {

    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    public static String getRandomUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (str.equalsIgnoreCase("null"))
            return true;
        for (int i = 0; i < len; i++) {
            switch (str.charAt(i)) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    // case '\b':
                    // case '\f':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * 获取主域名，即URL头
     *
     * @param url URL
     * @return Map<String, String>
     */
    public static Map<String, String> parseURLParam(String url) {

        Map<String, String> mapRequest = new HashMap<>();

        if (TextUtils.isEmpty(url)) {
            return mapRequest;
        }

        String[] arrSplit;

        String strUrlParam = TruncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (!arrSplitEqual[0].equals("")) {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 截取URL中的？之后的部分
     *
     * @param strUrl
     * @return
     */
    private static String TruncateUrlPage(String strUrl) {
        String strAllParam = null;
        String[] arrSplit = null;

        strUrl = strUrl.trim();

        arrSplit = strUrl.split("[?]");
        if (strUrl.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    public static String safetyText(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }

    public static String listToString(Integer[] list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            sb.append(list[i]).append(separator);
        }
        return sb.substring(0, sb.toString().length() - 1);
    }

    public String showCountText(Integer count) {
        if (count == null || count == 0) {
            return "";
        }
        return count < 100 ? count + "" : "99+";
    }

    public static String getFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int flag = fileName.lastIndexOf("/");
        String tempName = fileName.substring(flag > -1 ? flag + 1 : 0);
        if (tempName.startsWith("_1") && tempName.contains(".") && tempName.length() > 14) {
            tempName = tempName.substring(14);
        }
        return tempName.contains("?") ? tempName.substring(0, tempName.indexOf("?")) : tempName;
    }

    /**
     * 获取简短的文件名字
     *
     * @return String
     */
    public static String getShortFileName(String fileName) {
        String tempName = getFileName(fileName);
        if (TextUtils.isEmpty(tempName)) {
            return "";
        }
        return tempName.length() > 20 ?
                tempName.substring(0, 10) + ".." + tempName.substring(tempName.length() - 8) : tempName;
    }

    public static String join(CharSequence delimiter,
                              Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs : elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    public static boolean isOk(String status) {
        return StrKit.notBlank(status) && status.equals(Constants.STATUS_OK);
    }
}
