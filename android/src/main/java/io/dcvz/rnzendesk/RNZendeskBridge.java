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

    public RNZendeskBridge(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void initializeInstance(ReadableMap config) {
        Zendesk.INSTANCE.init(getReactApplicationContext(), config.getString("zendeskUrl"), config.getString("appId"), config.getString("clientId"));
        Support.INSTANCE.init(Zendesk.INSTANCE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void initialize(ReadableMap config) {
        initializeInstance(config);
        
        identifyJWT(config.getString("userId"));

        registerPushToken(config.getString("deviceToken"));
    }

    // MARK: - Indentification

    @ReactMethod
    public void identifyJWT(String token) {
        JwtIdentity identity = new JwtIdentity(token);
        Zendesk.INSTANCE.setIdentity(identity);
    }

    // MARK: - UI Methods

    @ReactMethod
    public void showHelpCenter(ReadableMap options) {
        HelpCenterActivity.builder().show(getReactApplicationContext());
    }
    
    @ReactMethod
    public void unregisterPushToken(ReadableMap config) {
        if(Zendesk.INSTANCE == null) {
            initializeInstance(config);
        }
        Zendesk.INSTANCE.provider().pushRegistrationProvider().unregisterDevice(new ZendeskCallback<Void>() {
            @Override
            public void onSuccess(final Void response) {

            }

            @Override
            public void onError(ErrorResponse errorResponse) {

            }
        });
    }

    @ReactMethod
    public void registerPushToken(String token) {
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
