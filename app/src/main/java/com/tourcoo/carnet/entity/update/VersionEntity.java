package com.tourcoo.carnet.entity.update;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :版本更新实体
 * @company :途酷科技
 * @date 2019年06月10日10:07
 * @Email: 971613168@qq.com
 */
public class VersionEntity implements Serializable {

    /**
     *
     *         "apkName": "1.3.1-1222-1556276682442.apk",
     *         "content": "喀什东路合肥打开了复活甲",
     *         "downloadUrl": "http://119.3.20.53:8090/iov/api/app/version/download/lastApk/1.3.1-1222-1556276682442.apk",
     *         "forceUpdate": false,
     *         "lastCode": 1222,
     *         "lastVersion": "1.3.1",
     *         "title": "哈萨的话"
     *
     */
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private boolean forceUpdate;
    private int lastCode;
    private String lastVersion;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String content;
    private String title;

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public int getLastCode() {
        return lastCode;
    }

    public void setLastCode(int lastCode) {
        this.lastCode = lastCode;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }
}
