package com.tourcoo.carnet.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.google.gson.Gson;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;


import cn.jpush.android.api.JPushInterface;


/**
 * JPush推送 自定义接收器
 */

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        TourcooLogUtil.i("点地方1asd击率");
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            /**
             * SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID ，可以通过此 ID 向对应的客户端发送消息和通知
             *
             * 注册JPush成功时回调，此时registrationID的值才不为空
             */
            TourcooLogUtil.i("点地方1asd击率");
//
//           InspectApplication.registrationID=bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG,"[PushReceiver] 接收Registration Id : "+InspectApplication.registrationID);


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            ToastUtil.show("点击率阿萨德");
            Log.d(TAG, "收到了自定义消息 Push 。");
            /**
             * 收到了自定义消息 Push 。
             *
             * SDK 对自定义消息，只是传递，不会有任何界面上的展示。
             *
             * 如果开发者想推送自定义消息 Push，则需要在 AndroidManifest.xml 里配置此 Receiver action，并且在自己写的 BroadcastReceiver 里接收处理。
             */
            Log.d(TAG, "[PushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            /**
             * SDK 1.2.9 以上版本支持。
             *
             * 保存服务器推送下来的附加字段。
             *
             * 这是个 JSON 字符串。对应 API 通知内容的 extras 字段。
             *
             * 对应 Portal 推送消息界面上的“可选设置”里的附加字段。
             *
             */
            String pushData = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!"".equals(pushData) && !"null".equals(pushData) && pushData != null) {

                Intent msgIntent = new Intent();
                msgIntent.setAction("com.tourcoo.carnet.intent.MESSAGE_RECEIVED");


//                PushModel model = new Gson().fromJson(pushData, PushModel.class);
                Log.d(TAG, new Gson().toJson(pushData));

                //自定义activity的处理
                //如果app的进程虽然仍然在但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，
                //如果直接启动对应Activity,再按Back键就不会返回MainActivity了。所以在启动对应Activity前，要先启动MainActivity
                TourcooLogUtil.i("点地方1asd击率");

            }


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver] 用户点击打开了通知");
            TourcooLogUtil.i("用户点击打开了通知");
            /**
             * 用户点击了通知。
             *
             * 一般情况下，用户不需要配置此 receiver action。
             *
             * 如果开发者在 AndroidManifest.xml 里未配置此 receiver action，那么，SDK 会默认打开应用程序的主 Activity，相当于用户点击桌面图标的效果。
             *
             * 如果开发者在 AndroidManifest.xml 里配置了此 receiver action，那么，当用户点击通知时，SDK 不会做动作。
             *
             * 开发者应该在自己写的 BroadcastReceiver 类里处理，比如打开某 Activity 。
             */
            String pushData = bundle.getString(JPushInterface.EXTRA_EXTRA);
            ToastUtil.show("点击率");
            TourcooLogUtil.i("点地方1asd击率");
//
            if (!"".equals(pushData) && !"null".equals(pushData) && pushData != null) {
                //接收到推送进行刷新
//                PushModel model = new Gson().fromJson(pushData, PushModel.class);

            }
        }
        TourcooLogUtil.i("点地方1asd击率");

    }

}


