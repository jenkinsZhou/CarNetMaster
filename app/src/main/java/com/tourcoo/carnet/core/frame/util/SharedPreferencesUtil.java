package com.tourcoo.carnet.core.frame.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tourcoo.carnet.CarNetApplication;

import java.util.Map;
import java.util.Set;

/**
 * @author :zhoujian
 * @description : SharedPreferences工具类
 * @company :翼迈科技
 * @date 2019年03月04日下午 03:56
 * @Email: 971613168@qq.com
 */
public class SharedPreferencesUtil {

    public static boolean put(String key, Object object) {
        if (CarNetApplication.getContext() == null) {
            return false;
        }
        return put(CarNetApplication.getContext(), CarNetApplication.getContext().getPackageName(), key, object);
    }

    /**
     * 存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param object
     * @return
     */
    public static boolean put(Context context, String fileName, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, ((Integer) object).intValue());
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Float) {
            editor.putFloat(key, ((Float) object).floatValue());
        } else if (object instanceof Long) {
            editor.putLong(key, ((Long) object).longValue());
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putStringSet(key, (Set<String>) object);
        }
        return editor.commit();
    }

    public static Object get(String key, Object def) {
        return get(CarNetApplication.getInstance(), CarNetApplication.getInstance().getPackageName(), key, def);
    }

    /**
     * 获取存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param def
     * @return
     */
    public static Object get(Context context, String fileName, String key, Object def) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (def instanceof String) {
            return sp.getString(key, def.toString());
        } else if (def instanceof Integer) {
            return sp.getInt(key, ((Integer) def).intValue());
        } else if (def instanceof Boolean) {
            return sp.getBoolean(key, ((Boolean) def).booleanValue());
        } else if (def instanceof Float) {
            return sp.getFloat(key, ((Float) def).floatValue());
        } else if (def instanceof Long) {
            return sp.getLong(key, ((Long) def).longValue());
        } else if (def instanceof Set) {
            return sp.getStringSet(key, (Set<String>) def);
        }
        return def;
    }

    public static boolean remove(Context context, String key) {
        if (context == null) {
            return false;
        }
        return remove(context, context.getPackageName(), key);
    }

    public static boolean remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean clearAll(Context context) {
        if (context == null) {
            return false;
        }
        return clearAll(context, context.getPackageName());
    }

    public static boolean clearAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }

    public static boolean contains(Context context, String key) {
        if (context == null) {
            return false;
        }
        return contains(context, context.getPackageName(), key);
    }

    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public static Map<String, ?> getAll(Context context) {
        if (context == null) {
            return null;
        }
        return getAll(context, context.getPackageName());
    }

    public static Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
