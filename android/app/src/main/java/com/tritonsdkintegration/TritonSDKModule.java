package com.tritonsdkintegration;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tritondigital.player.TritonPlayer;
import com.tritondigital.player.MediaPlayer;

import android.util.Log;
import android.content.Context;
import android.media.AudioManager;

public class TritonSDKModule extends ReactContextBaseJavaModule implements TritonPlayer.OnStateChangedListener {
    ReactApplicationContext context;
    TritonPlayer player;
    private static final String EVENT_STATE_CHANGED = "stateChanged";

    TritonSDKModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
    }

    @ReactMethod
    public void play(String mount) {
        Log.d("TritonSDK Module", "Play the following mount: " + mount);
        // Create the player settings.
        Bundle settings = new Bundle();
        settings.putString(TritonPlayer.SETTINGS_STATION_BROADCASTER, "Triton Digital");
        settings.putString(TritonPlayer.SETTINGS_STATION_NAME, "TRITONRADIOMUSIC");
        settings.putString(TritonPlayer.SETTINGS_STATION_MOUNT, mount);

        this.player = new TritonPlayer(this.context.getApplicationContext(), settings);
        this.player.setOnStateChangedListener(this);

        this.player.play();
    }

    @ReactMethod
    public void stop() {
        Log.d("TritonSDK Module", "Stopping the Triton Player");
        this.player.stop();
    }


    @Override
    public String getName() {
        return "TritonSDKModule";
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onStateChanged(MediaPlayer mediaPlayer, int state) {
        String playerState = "Stopped";
        WritableMap map = Arguments.createMap();

        switch (state) {
            case 201:
                playerState = "Connecting";
                break;
            case 202:
                playerState = "Error";
                break;
            case 203:
                playerState = "Playing";
                break;
            case 205:
                playerState = "Stopped";
                break;
            case 206:
                playerState = "Paused";
                break;
            default:
                playerState = "Stopped";
        }

        map.putString("state", playerState);

        sendEvent(EVENT_STATE_CHANGED, map);

    }

    private AudioManager getAudioManager() {
        return (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }


}