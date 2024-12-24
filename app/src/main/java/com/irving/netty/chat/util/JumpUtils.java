package com.irving.netty.chat.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 /**
 * @author luojun
 * @version 1.0.0
 * @ClassName JumpUtils
 * @Description 界面的跳转工具类
 * @createTime 2021/11/14 5:45
 */

@SuppressLint("NewApi")
public class JumpUtils {

    /**
     * <p><B>方法:</B><br/> startForwardActivity </p><br/>
     * <p><B>描述:</B><br/> 界面的跳转工具类</p>
     *
     * @param context
     * @param forwardActivity 设定文件
     */
    public static void startForwardActivity(Activity context, Class<?> forwardActivity) {
        startForwardActivity(context, forwardActivity, false);
    }

    public static void startForwardActivity(Activity context, Class<?> forwardActivity, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForwardActivity(Activity context, Class<?> forwardActivity, Bundle bundle, Boolean isFinish, int animin, int animout) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
        try {
            context.overridePendingTransition(animin, animout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startForwardActivity(Context context, Class<?> forwardActivity, Bundle bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (isFinish) {
            ((AppCompatActivity)context).finish();
        }
    }

    public static void startActivity(Activity context, Class<?> forwardActivity, Bundle bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForResultActivity(Activity context, Class<?> forwardActivity, int requestCode, Bundle bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForResultActivity(Activity context, Class<?> forwardActivity, int requestCode, Bundle bundle, Boolean isFinish, int animin, int animout) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
        try {
            context.overridePendingTransition(animin, animout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startForResultActivity2(Activity context, Class<?> forwardActivity, int requestCode, Bundle bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
    }

    //fragment 跳转回调
    public static void startForResultFragment(Activity context, Fragment fragment, Class<?> forwardActivity, int requestCode, Bundle bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
    }

}
