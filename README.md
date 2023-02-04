# htwinkle.cn.app

htwinkle.cn app版本信息

# 安装app到手机

```shell
adb install WeLife_V10.0.24_release.apk
```

## 证书处理

```shell
openssl base64 < some_signing_key.jks | tr -d '\n' | tee some_signing_key.jks.base64.txt
```
