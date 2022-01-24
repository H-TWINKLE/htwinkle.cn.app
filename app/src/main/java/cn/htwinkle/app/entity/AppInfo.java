package cn.htwinkle.app.entity;

import androidx.appcompat.app.AppCompatActivity;

public class AppInfo {
    /**
     * 应用名字
     */
    private String name;
    /**
     * 打开的UI
     */
    private Class<?> tClass;
    /**
     * 描述
     */
    private String description;
    /**
     * 图片信息
     */
    private String imgUrl;

    /**
     * 图片信息
     */
    private Integer resourcesId;

    private String[] permission;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> gettClass() {
        return tClass;
    }

    public void settClass(Class<?> tClass) {
        this.tClass = tClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Integer resourcesId) {
        this.resourcesId = resourcesId;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }
}
