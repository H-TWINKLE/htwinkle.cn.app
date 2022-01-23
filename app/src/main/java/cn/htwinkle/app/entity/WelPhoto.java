package cn.htwinkle.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WelPhoto implements java.io.Serializable {


    /**
     * msg : 我们的生活
     * num : 10
     * plate : 2
     * state : ok
     * list : [{"pictureDate":"2022-01-23 02:00:53","pictureId":2703900,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g3/M09/0C/0B/ChMlV17yyMWIc6UUAE1U65mweVMAAVMsgOOACcATVUD925.jpg","picturePlate":2,"pictureName":"水上的小船倒映自然风景","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:34","pictureId":2702860,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g6/M00/0B/06/ChMkKmFuI9KIfpShACKAPIciXzIAAUuQAMY_PUAIoBU441.jpg","picturePlate":2,"pictureName":"秋天的森林","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:09","pictureId":2701191,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g3/M0A/0C/09/ChMlWF7yuC2IK5FmABz91ttueWoAAVMUwDVJaEAHP3u659.jpg","picturePlate":2,"pictureName":"水上的小船倒映自然风景","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:59","pictureId":2704233,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g2/M00/08/0A/ChMlWl7HoMCIKdXnADsHQDuoTKcAAPXmQCKvHIAOwdY560.jpg","picturePlate":2,"pictureName":"最美的雪山","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:42","pictureId":2703283,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g6/M00/03/09/ChMkKV-arYWIHOyVACzzMHTfkBQAAEgzgO-qXcALPNI256.jpg","picturePlate":2,"pictureName":"绚丽霞光风景","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:45","pictureId":2703421,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g6/M00/0B/0B/ChMkKV-IC5WIe9GmAED1jukH0KsAADuMAABKnsAQPWm128.jpg","picturePlate":2,"pictureName":"大美七彩丹霞地貌高第辑","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:06","pictureId":2700972,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g5/M00/0D/00/ChMkJl7-tkiIPqNVADKy_dat9mAAAwkagI-WtEAMrMV914.jpg","picturePlate":2,"pictureName":"红色海岸美景","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 01:59:39","pictureId":2699859,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g6/M00/06/0D/ChMkKmE1dbaIU9sBABJ8hdw7f0UAATcJwLMsVQAEnyd754.jpg","picturePlate":2,"pictureName":"海边灯塔","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:59","pictureId":2704242,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g2/M00/06/0C/ChMlWV7DRWeIFvnnADpu9MT6gsEAAPQVAEI-x4AOm8M077.jpg","picturePlate":2,"pictureName":"优美自然风景","pictureTypes":"fengjing"},{"pictureDate":"2022-01-23 02:00:15","pictureId":2701661,"pictureHost":"http://desk.zol.com.cn/","pictureUrl":"http://desk-fd.zol-img.com.cn/t_s1920x1080c/g2/M00/01/04/ChMlWV7fGcWIem0-ABil3mfg8VEAAP6HAGDrd4AGKX2164.jpg","picturePlate":2,"pictureName":"唯美农田风光","pictureTypes":"fengjing"}]
     * type : fengjing
     */

    private String msg;
    private Integer num;
    private Integer plate;
    private String state;
    private String type;
    private List<ListBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPlate() {
        return plate;
    }

    public void setPlate(Integer plate) {
        this.plate = plate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ListBean> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * pictureDate : 2022-01-23 02:00:53
         * pictureId : 2703900
         * pictureHost : http://desk.zol.com.cn/
         * pictureUrl : http://desk-fd.zol-img.com.cn/t_s1920x1080c/g3/M09/0C/0B/ChMlV17yyMWIc6UUAE1U65mweVMAAVMsgOOACcATVUD925.jpg
         * picturePlate : 2
         * pictureName : 水上的小船倒映自然风景
         * pictureTypes : fengjing
         */

        private String pictureDate;
        private Integer pictureId;
        private String pictureHost;
        private String pictureUrl;
        private Integer picturePlate;
        private String pictureName;
        private String pictureTypes;

        public String getPictureDate() {
            return pictureDate;
        }

        public void setPictureDate(String pictureDate) {
            this.pictureDate = pictureDate;
        }

        public Integer getPictureId() {
            return pictureId;
        }

        public void setPictureId(Integer pictureId) {
            this.pictureId = pictureId;
        }

        public String getPictureHost() {
            return pictureHost;
        }

        public void setPictureHost(String pictureHost) {
            this.pictureHost = pictureHost;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public Integer getPicturePlate() {
            return picturePlate;
        }

        public void setPicturePlate(Integer picturePlate) {
            this.picturePlate = picturePlate;
        }

        public String getPictureName() {
            return pictureName;
        }

        public void setPictureName(String pictureName) {
            this.pictureName = pictureName;
        }

        public String getPictureTypes() {
            return pictureTypes;
        }

        public void setPictureTypes(String pictureTypes) {
            this.pictureTypes = pictureTypes;
        }
    }
}
