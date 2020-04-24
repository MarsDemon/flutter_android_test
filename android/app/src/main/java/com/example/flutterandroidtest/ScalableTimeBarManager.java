package com.example.flutterandroidtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.example.flutterandroidtest.scaletimebar.ScalableTimebarView;

public class ScalableTimeBarManager implements PlatformView, MethodChannel.MethodCallHandler{
    private final ScalableTimebarView nativeTimeBarView;
    Context context;
    private final MethodChannel methodChannel;

    public ScalableTimeBarManager(Context context, BinaryMessenger messenger, int id, Map<String, Object> params) {
        this.context = context;
        ScalableTimebarView myNativeView = new ScalableTimebarView(context);
        methodChannel = new MethodChannel(messenger, "plugins.ls.view/scale_time_bar");
        methodChannel.setMethodCallHandler(this);
        setTimeForTimeline(myNativeView, null);
        // 时间轴 缩放监听
        myNativeView.setOnBarScaledListener(new ScalableTimebarView.OnBarScaledListener() {
            @Override
            public void onBarScaled(long screenLeftTime, long screenRightTime, long currentTime) {
                methodChannel.invokeMethod("current_time", currentTime);
            }

            @Override
            public void onBarScaleFinish(long screenLeftTime, long screenRightTime, long currentTime) {
            }
        });

        // 时间轴 拖动监听
        myNativeView.setOnBarMoveListener(new ScalableTimebarView.OnBarMoveListener() {
            @Override
            public void onBarMove(long screenLeftTime, long screenRightTime, long currentTime) {
                Map map = new HashMap();
                map.put("time", currentTime);
                methodChannel.invokeMethod("current_time", map);
            }

            @Override
            public void OnBarMoveFinish(long screenLeftTime, long screenRightTime, long currentTime) {
                Map map = new HashMap();
                map.put("move_bar_time", currentTime);
                methodChannel.invokeMethod("move_bar", map);
            }
        });
        this.nativeTimeBarView = myNativeView;

    }

    @Override
    public View getView() {
        return nativeTimeBarView;
    }

    @Override
    public void dispose() {

    }

    // 用于处理flutter与原生view交互的
    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        // 在接口的回调方法中可以接收到来自Flutter的调用
        System.out.println("ScalableTimeBarManager.onMethodCall == " + call.toString());
        if ("calendar_choose".equals(call.method)) {
            Map map = (HashMap)call.arguments;
            int time = (int) map.get("timestamp");
            Map map1 = new HashMap();
            map1.put("time", time);
            methodChannel.invokeMethod("current_time", map1);
//            setTimeForTimeline(nativeTimeBarView, DateFormat.s);
        } else if ("t_video_file".equals(call.method)) {
            Map map = (HashMap)call.arguments;
            long time = (long) map.get("t_video_file");
        }
    }

    public void setTimeForTimeline(ScalableTimebarView myNativeView, Calendar calendar) {
        long currentRealDateTime;
        long zero;
        long timeBarRightEndPointTime;
        long timeBarLeftEndPointTime;
        long timeBarCursorCurrentTime;
        if (calendar == null) {
            currentRealDateTime = System.currentTimeMillis();
        } else {
            currentRealDateTime = calendar.getTimeInMillis();
        }
        zero = currentRealDateTime / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        timeBarLeftEndPointTime = zero;
        timeBarRightEndPointTime = zero + 24 * 60 * 60 * 1000 * 2;
        timeBarCursorCurrentTime = currentRealDateTime;
        myNativeView.initTimebarLengthAndPosition(timeBarLeftEndPointTime,
                timeBarRightEndPointTime, timeBarCursorCurrentTime);
    }
}