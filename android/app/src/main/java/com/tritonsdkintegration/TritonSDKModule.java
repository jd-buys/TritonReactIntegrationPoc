package com.tritonsdkintegration;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.os.Bundle;
import com.tritondigital.player.TritonPlayer;
import android.util.Log;
import android.content.Context;

public class TritonSDKModule extends ReactContextBaseJavaModule {
    Context context;
    TritonPlayer player;

    TritonSDKModule(ReactApplicationContext context) {
        super(context);
        this.context = context.getApplicationContext();
    }

        @ReactMethod
        public void play (String mount){
        Log.d("TritonSDK Module", "Play the following mount: " + mount);
        // Create the player settings.
        Bundle settings = new Bundle();
        settings.putString(TritonPlayer.SETTINGS_STATION_BROADCASTER, "Triton Digital");
        settings.putString(TritonPlayer.SETTINGS_STATION_NAME, "TRITONRADIOMUSIC");
        settings.putString(TritonPlayer.SETTINGS_STATION_MOUNT, mount);

        this.player = new TritonPlayer(this.context, settings);

        this.player.play();
    }

    @ReactMethod
    public void stop (){
        Log.d("TritonSDK Module", "Stopping the Triton Player");
        this.player.stop();
    }


        @Override
        public String getName () {
        return "TritonSDKModule";
    }

}