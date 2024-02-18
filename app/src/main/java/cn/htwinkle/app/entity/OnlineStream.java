package cn.htwinkle.app.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class OnlineStream {

    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * server
     */
    @JSONField(name = "server")
    private String server;
    /**
     * service
     */
    @JSONField(name = "service")
    private String service;
    /**
     * pid
     */
    @JSONField(name = "pid")
    private String pid;
    /**
     * streams
     */
    @JSONField(name = "streams")
    private List<StreamsDTO> streams;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<StreamsDTO> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamsDTO> streams) {
        this.streams = streams;
    }

    public static class StreamsDTO implements Serializable {
        /**
         * id
         */
        @JSONField(name = "id")
        private String id;
        /**
         * name
         */
        @JSONField(name = "name")
        private String name;
        /**
         * vhost
         */
        @JSONField(name = "vhost")
        private String vhost;
        /**
         * app
         */
        @JSONField(name = "app")
        private String app;
        /**
         * tcUrl
         */
        @JSONField(name = "tcUrl")
        private String tcUrl;
        /**
         * url
         */
        @JSONField(name = "url")
        private String url;
        /**
         * liveMs
         */
        @JSONField(name = "live_ms")
        private Long liveMs;
        /**
         * clients
         */
        @JSONField(name = "clients")
        private Integer clients;
        /**
         * frames
         */
        @JSONField(name = "frames")
        private Integer frames;
        /**
         * sendBytes
         */
        @JSONField(name = "send_bytes")
        private Integer sendBytes;
        /**
         * recvBytes
         */
        @JSONField(name = "recv_bytes")
        private Integer recvBytes;
        /**
         * kbps
         */
        @JSONField(name = "kbps")
        private KbpsDTO kbps;
        /**
         * publish
         */
        @JSONField(name = "publish")
        private PublishDTO publish;
        /**
         * video
         */
        @JSONField(name = "video")
        private VideoDTO video;
        /**
         * audio
         */
        @JSONField(name = "audio")
        private Object audio;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVhost() {
            return vhost;
        }

        public void setVhost(String vhost) {
            this.vhost = vhost;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getTcUrl() {
            return tcUrl;
        }

        public void setTcUrl(String tcUrl) {
            this.tcUrl = tcUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getLiveMs() {
            return liveMs;
        }

        public void setLiveMs(Long liveMs) {
            this.liveMs = liveMs;
        }

        public Integer getClients() {
            return clients;
        }

        public void setClients(Integer clients) {
            this.clients = clients;
        }

        public Integer getFrames() {
            return frames;
        }

        public void setFrames(Integer frames) {
            this.frames = frames;
        }

        public Integer getSendBytes() {
            return sendBytes;
        }

        public void setSendBytes(Integer sendBytes) {
            this.sendBytes = sendBytes;
        }

        public Integer getRecvBytes() {
            return recvBytes;
        }

        public void setRecvBytes(Integer recvBytes) {
            this.recvBytes = recvBytes;
        }

        public KbpsDTO getKbps() {
            return kbps;
        }

        public void setKbps(KbpsDTO kbps) {
            this.kbps = kbps;
        }

        public PublishDTO getPublish() {
            return publish;
        }

        public void setPublish(PublishDTO publish) {
            this.publish = publish;
        }

        public VideoDTO getVideo() {
            return video;
        }

        public void setVideo(VideoDTO video) {
            this.video = video;
        }

        public Object getAudio() {
            return audio;
        }

        public void setAudio(Object audio) {
            this.audio = audio;
        }

        public static class KbpsDTO implements Serializable {
            /**
             * recv30s
             */
            @JSONField(name = "recv_30s")
            private Integer recv30s;
            /**
             * send30s
             */
            @JSONField(name = "send_30s")
            private Integer send30s;

            public Integer getRecv30s() {
                return recv30s;
            }

            public void setRecv30s(Integer recv30s) {
                this.recv30s = recv30s;
            }

            public Integer getSend30s() {
                return send30s;
            }

            public void setSend30s(Integer send30s) {
                this.send30s = send30s;
            }
        }

        public static class PublishDTO implements Serializable {
            /**
             * active
             */
            @JSONField(name = "active")
            private Boolean active;
            /**
             * cid
             */
            @JSONField(name = "cid")
            private String cid;

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }
        }

        public static class VideoDTO implements Serializable {
            /**
             * codec
             */
            @JSONField(name = "codec")
            private String codec;
            /**
             * profile
             */
            @JSONField(name = "profile")
            private String profile;
            /**
             * level
             */
            @JSONField(name = "level")
            private String level;
            /**
             * width
             */
            @JSONField(name = "width")
            private Integer width;
            /**
             * height
             */
            @JSONField(name = "height")
            private Integer height;

            public String getCodec() {
                return codec;
            }

            public void setCodec(String codec) {
                this.codec = codec;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }
        }
    }
}
