package io.dcvz.rnzendesk;

import android.os.Build;

import androidx.annotation.RequiresApi;

import zendesk.core.Zendesk;
import zendesk.core.JwtIdentity;
import zendesk.support.Support;
import zendesk.support.guide.HelpCenterActivity;

import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNZendeskBridge extends ReactContextBaseJavaModule {
    public static final String MODULE_NAME = "RNZendesk";

    private static ReactApplicationContext context;

    public RNZendeskBridge(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void initialize(ReadableMap config) {
        Zendesk.INSTANCE.init(getReactApplicationContext(), config.getString("zendeskUrl"), config.getString("appId"), config.getString("clientId"));
        Support.INSTANCE.init(Zendesk.INSTANCE);
        
        JwtIdentity identity = new JwtIdentity(config.getString("userId"));
        Zendesk.INSTANCE.setIdentity(identity);
    }

    @ReactMethod
    public void showHelpCenter(ReadableMap options) {
        HelpCenterActivity.builder().show(context.getCurrentActivity());
    }
    
    @ReactMethod
    public void unregisterPushToken() {
        if(Zendesk.INSTANCE.provider() != null) {
            Zendesk.INSTANCE.provider().pushRegistrationProvider().unregisterDevice(new ZendeskCallback<Void>() {
                @Override
                public void onSuccess(final Void response) {

                }

                @Override
                public void onError(ErrorResponse errorResponse) {

                }
            });
        }
    }

    @ReactMethod
     public void registerPushToken(String token, final) {
        if(Zendesk.INSTANCE.provider() != null) {
            Zendesk.INSTANCE.provider().pushRegistrationProvider().registerWithDeviceIdentifier(token, new ZendeskCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    
                }

                @Override
                public void onError(ErrorResponse errorResponse) { 
                    
                }
            });
        }
    }
}
