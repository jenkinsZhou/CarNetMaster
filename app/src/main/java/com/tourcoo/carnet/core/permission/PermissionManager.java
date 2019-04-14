package com.tourcoo.carnet.core.permission;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author :JenkinsZhou
 * @description :权限管理
 * @company :途酷科技
 * @date 2019年03月22日11:31
 * @Email: 971613168@qq.com
 */
public class PermissionManager {

    /**
     * @param context return true:已经获取权限
     * @return false: 未获取权限，主动请求权限
     */
    public static boolean checkPermission(Context context, String[] perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 请求权限
     *
     * @param context
     */
    public static void requestPermission(Activity context, String tip, int requestCode, String[] perms) {
        EasyPermissions.requestPermissions(context, tip, requestCode, perms);
    }

    /**
     * 获取位置权限
     *
     * @param context
     */
    public static void requestPermissionLocate(Activity context) {
        PermissionManager.requestPermission(context, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
    }


    /**
     * 检查是否拥有定位权限
     */
    public static boolean checkPermissionLocate(Activity context) {
        return PermissionManager.checkPermission(context, PermissionConstance.PERMS_LOCATE);
    }

    /**
     * 请求权限 Fragment
     *
     * @param context
     */
    public static void requestPermission(Fragment context, String tip, int requestCode, String[] perms) {
        EasyPermissions.requestPermissions(context, tip, requestCode, perms);
    }


}
