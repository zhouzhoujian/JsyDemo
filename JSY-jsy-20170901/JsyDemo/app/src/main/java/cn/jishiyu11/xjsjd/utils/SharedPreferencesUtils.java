package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author A18ccms A18ccms_gmail_com
 * @version V1.0
 * @Title: SharedPreferencesUtils.java
 * @Package
 * @Description: TODO 保存基本信息
 * @date 2016-3-8 下午4:16:29
 */
public class SharedPreferencesUtils {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        if (object == null || context == null || key == null)
            return;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        if (defaultObject == null || context == null || key == null)
            return "";
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            if (!sp.getString(key, (String) defaultObject).equals("")) {
                return sp.getString(key, (String) defaultObject);
            } else
                return "";
        } else if (defaultObject instanceof Integer) {
            if (!"".equals(sp.getInt(key, (Integer) defaultObject) + "")) {
                return sp.getInt(key, (Integer) defaultObject);
            } else
                return 0;
        } else if (defaultObject instanceof Boolean) {
            if (!"".equals(sp.getBoolean(key, (Boolean) defaultObject) + "")) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else
                return false;
        } else if (defaultObject instanceof Float) {
            if (!"".equals(sp.getFloat(key, (Float) defaultObject) + "")) {
                return sp.getFloat(key, (Float) defaultObject);
            } else
                return 0;
        } else if (defaultObject instanceof Long) {
            if (!"".equals(sp.getLong(key, (Long) defaultObject) + "")) {
                return sp.getLong(key, (Long) defaultObject);
            } else
                return 0;
        }
        return "";
    }

    public static String saveInfo(Context context, List<Map<String, Object>> datas) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> itemMap = datas.get(i);
            Iterator<Entry<String, Object>> iterator = itemMap.entrySet().iterator();
            JSONObject object = new JSONObject();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException ignored) {

                }
            }
            mJsonArray.put(object);
        }
        return mJsonArray.toString();
    }

    public static List<Map<String, Object>> getInfo(Context context, String result) {
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, Object> itemMap = new HashMap<>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException ignored) {

        }
        return datas;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException ignored) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException ignored) {
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException ignored) {
            }
            editor.commit();
        }
    }

    public static void shareremvo(Context context, String key) {
        SharedPreferences mShared = context.getSharedPreferences(FILE_NAME, 0);
        Editor editor = mShared.edit();
        editor.remove(key);
        editor.commit();
    }


    /**
     * 登出 @Title: loginSuccess @author: xusonghui @Description:
     * TODO(这里用一句话描述这个方法的作用) @param: @param content @param: @param
     * myLoginData @return: void @throws
     */
    public static void logoutSuccess(Context content) {
        SharedPreferencesUtils.shareremvo(content, "username");
        SharedPreferencesUtils.shareremvo(content, "uid");
        SharedPreferencesUtils.shareremvo(content, "password");
        SharedPreferencesUtils.shareremvo(content, "realname");
//        SharedPreferencesUtils.shareremvo(content, "ChinalIDCardId");
//        SharedPreferencesUtils.shareremvo(content, "UserChinaName");
    }
}
