package cn.htwinkle.app.kit;

import com.alibaba.fastjson.JSONObject;

import cn.htwinkle.app.entity.ReleaseLatest;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

public enum VersionUpdateKit {
    INSTANCE;

    /**
     * 获取最新的资源文件
     *
     * @return List
     */
    public ReleaseLatest getGithubAssertsList(String url) {
        String result = HttpUtil.get(url);
        if (StrUtil.isEmpty(result)) {
            return null;
        }
        ReleaseLatest latest = JSONObject.parseObject(result, ReleaseLatest.class);
        if (latest == null || CollUtil.isEmpty(latest.getAssets())) {
            return null;
        }
        return latest;
    }


}
