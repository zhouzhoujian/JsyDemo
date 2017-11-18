package cn.jishiyu11.xjsjd.base;

import android.app.Activity;

import java.util.Stack;

/**
 * 类功能介绍
 *
 * @Title: BaseActivityManager.java
 * @Description: TODO 堆栈管理器
 * @author: 秦国良
 * @data: 2016年5月16日 下午8:08:34
 * @ModifiedPerson:
 * @ModifiedPersonData：2016年5月16日下午8:08:34 @ModifyRemarks：
 * @version: V1.1.0
 * @Copyright 沙小僧
 */
@SuppressWarnings("EqualsBetweenInconvertibleTypes")
public class BaseActivityManager {

    private static Stack<Activity> activityStack;
    private static BaseActivityManager instance;

    private BaseActivityManager() {

    }

    public static BaseActivityManager getActivityManager() {
        if (instance == null) {
            instance = new BaseActivityManager();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
        }
    }

    public void remove(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public Activity currentActivity() {
        if ((activityStack == null) || (activityStack.size() == 0)) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    //销毁所有
    public void finishAll() {
        for (Activity activity : activityStack) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void finshAllActivityExceptOne(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (!activityStack.get(i).getClass().equals(cls)) {
                activityStack.get(i).finish();
            }
        }
    }

    public void popActivityOne(Class<?> cls) {
        if ("".equals(cls) || cls == null)
            return;

        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                activityStack.get(i).finish();
            }

            // String name1 = activityStack.get(i).getClass().getName();
            // String name2 = cls.getName();
            // if(name1.equals(name2)){
            // activityStack.get(i).finish();
            // }
            // }
        }
    }

    public void exitApp() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    public Boolean isStart(Class<?> cls) {
        Boolean start = false;
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                start = true;
                break;
            }
        }
        return start;
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
