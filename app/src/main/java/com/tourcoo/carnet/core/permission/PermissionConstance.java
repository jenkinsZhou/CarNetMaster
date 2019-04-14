package com.tourcoo.carnet.core.permission;

import android.Manifest;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * @author :JenkinsZhou
 * @description :权限相关常量
 * @company :途酷科技
 * @date 2019年03月22日11:23
 * @Email: 971613168@qq.com
 */
public class PermissionConstance {
    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public final static int PERMISSION_CODE_STORAGE = 100;
    public final static int PERMISSION_CODE_LOCATE = 101;

    public final static String TIP_PERMISSION_STORAGE = "为了正常使用，请允许存储权限";

    public final static String TIP_PERMISSION_LOCATE = "为了正常使用，请允许定位权限";
    /**
     * 存储相关权限
     */
    public final static String[] PERMS_STORAGE = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    /**
     * 定位相关权限
     */
    public final static String[] PERMS_LOCATE = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION,WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    /**
     * 相机，图库的请求回调code
     */
    public final static int PICTURE_CODE = 10;
    public final static int GALLERY_CODE = 11;
}
