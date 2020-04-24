package com.example.flutterandroidtest;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;

public class ScalableTimeBarFlutterPlugin implements FlutterPlugin {

    public ScalableTimeBarFlutterPlugin(FlutterEngine flutterEngine) {
        final String key = ScalableTimeBarFlutterPlugin.class.getCanonicalName();
        if (flutterEngine.getPlugins().has(ScalableTimeBarFlutterPlugin.class)) return;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        final String key = ScalableTimeBarFlutterPlugin.class.getCanonicalName();
        binding.getPlatformViewRegistry().registerViewFactory("scale_time_bar", new ScalableTimeBarFactory(binding.getBinaryMessenger()));
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }
}
