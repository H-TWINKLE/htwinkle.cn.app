package cn.htwinkle.app.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ReleaseLatest {

    /**
     * url
     */
    @JSONField(name = "url")
    private String url;
    /**
     * assetsUrl
     */
    @JSONField(name = "assets_url")
    private String assetsUrl;
    /**
     * uploadUrl
     */
    @JSONField(name = "upload_url")
    private String uploadUrl;
    /**
     * htmlUrl
     */
    @JSONField(name = "html_url")
    private String htmlUrl;
    /**
     * id
     */
    @JSONField(name = "id")
    private Integer id;
    /**
     * author
     */
    @JSONField(name = "author")
    private AuthorDTO author;
    /**
     * nodeId
     */
    @JSONField(name = "node_id")
    private String nodeId;
    /**
     * tagName
     */
    @JSONField(name = "tag_name")
    private String tagName;
    /**
     * targetCommitish
     */
    @JSONField(name = "target_commitish")
    private String targetCommitish;
    /**
     * name
     */
    @JSONField(name = "name")
    private String name;
    /**
     * draft
     */
    @JSONField(name = "draft")
    private Boolean draft;
    /**
     * prerelease
     */
    @JSONField(name = "prerelease")
    private Boolean prerelease;
    /**
     * createdAt
     */
    @JSONField(name = "created_at")
    private String createdAt;
    /**
     * publishedAt
     */
    @JSONField(name = "published_at")
    private String publishedAt;
    /**
     * assets
     */
    @JSONField(name = "assets")
    private List<AssetsDTO> assets;
    /**
     * tarballUrl
     */
    @JSONField(name = "tarball_url")
    private String tarballUrl;
    /**
     * zipballUrl
     */
    @JSONField(name = "zipball_url")
    private String zipballUrl;
    /**
     * body
     */
    @JSONField(name = "body")
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }

    public void setAssetsUrl(String assetsUrl) {
        this.assetsUrl = assetsUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTargetCommitish() {
        return targetCommitish;
    }

    public void setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Boolean getPrerelease() {
        return prerelease;
    }

    public void setPrerelease(Boolean prerelease) {
        this.prerelease = prerelease;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<AssetsDTO> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsDTO> assets) {
        this.assets = assets;
    }

    public String getTarballUrl() {
        return tarballUrl;
    }

    public void setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
    }

    public String getZipballUrl() {
        return zipballUrl;
    }

    public void setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static class AuthorDTO {
        /**
         * login
         */
        @JSONField(name = "login")
        private String login;
        /**
         * id
         */
        @JSONField(name = "id")
        private Integer id;
        /**
         * nodeId
         */
        @JSONField(name = "node_id")
        private String nodeId;
        /**
         * avatarUrl
         */
        @JSONField(name = "avatar_url")
        private String avatarUrl;
        /**
         * gravatarId
         */
        @JSONField(name = "gravatar_id")
        private String gravatarId;
        /**
         * url
         */
        @JSONField(name = "url")
        private String url;
        /**
         * htmlUrl
         */
        @JSONField(name = "html_url")
        private String htmlUrl;
        /**
         * followersUrl
         */
        @JSONField(name = "followers_url")
        private String followersUrl;
        /**
         * followingUrl
         */
        @JSONField(name = "following_url")
        private String followingUrl;
        /**
         * gistsUrl
         */
        @JSONField(name = "gists_url")
        private String gistsUrl;
        /**
         * starredUrl
         */
        @JSONField(name = "starred_url")
        private String starredUrl;
        /**
         * subscriptionsUrl
         */
        @JSONField(name = "subscriptions_url")
        private String subscriptionsUrl;
        /**
         * organizationsUrl
         */
        @JSONField(name = "organizations_url")
        private String organizationsUrl;
        /**
         * reposUrl
         */
        @JSONField(name = "repos_url")
        private String reposUrl;
        /**
         * eventsUrl
         */
        @JSONField(name = "events_url")
        private String eventsUrl;
        /**
         * receivedEventsUrl
         */
        @JSONField(name = "received_events_url")
        private String receivedEventsUrl;
        /**
         * type
         */
        @JSONField(name = "type")
        private String type;
        /**
         * siteAdmin
         */
        @JSONField(name = "site_admin")
        private Boolean siteAdmin;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        public String getGistsUrl() {
            return gistsUrl;
        }

        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        public String getStarredUrl() {
            return starredUrl;
        }

        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        public String getEventsUrl() {
            return eventsUrl;
        }

        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getSiteAdmin() {
            return siteAdmin;
        }

        public void setSiteAdmin(Boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }
    }

    public static class AssetsDTO {
        /**
         * url
         */
        @JSONField(name = "url")
        private String url;
        /**
         * id
         */
        @JSONField(name = "id")
        private Integer id;
        /**
         * nodeId
         */
        @JSONField(name = "node_id")
        private String nodeId;
        /**
         * name
         */
        @JSONField(name = "name")
        private String name;
        /**
         * label
         */
        @JSONField(name = "label")
        private String label;
        /**
         * uploader
         */
        @JSONField(name = "uploader")
        private AuthorDTO uploader;
        /**
         * contentType
         */
        @JSONField(name = "content_type")
        private String contentType;
        /**
         * state
         */
        @JSONField(name = "state")
        private String state;
        /**
         * size
         */
        @JSONField(name = "size")
        private Integer size;
        /**
         * downloadCount
         */
        @JSONField(name = "download_count")
        private Integer downloadCount;
        /**
         * createdAt
         */
        @JSONField(name = "created_at")
        private String createdAt;
        /**
         * updatedAt
         */
        @JSONField(name = "updated_at")
        private String updatedAt;
        /**
         * browserDownloadUrl
         */
        @JSONField(name = "browser_download_url")
        private String browserDownloadUrl;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public AuthorDTO getUploader() {
            return uploader;
        }

        public void setUploader(AuthorDTO uploader) {
            this.uploader = uploader;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(Integer downloadCount) {
            this.downloadCount = downloadCount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getBrowserDownloadUrl() {
            return browserDownloadUrl;
        }

        public void setBrowserDownloadUrl(String browserDownloadUrl) {
            this.browserDownloadUrl = browserDownloadUrl;
        }
    }
}
