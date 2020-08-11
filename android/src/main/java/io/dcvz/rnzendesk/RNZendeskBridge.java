package io.dcvz.rnzendesk;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import zendesk.commonui.UiConfig;
import zendesk.core.Zendesk;
import zendesk.core.Identity;
import zendesk.core.JwtIdentity;
import zendesk.core.AnonymousIdentity;
import zendesk.support.Support;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.request.RequestActivity;
import zendesk.support.requestlist.RequestListActivity;

import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;
import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.ArrayList;
import java.util.Locale;

public class RNZendeskBridge extends ReactContextBaseJavaModule {
    public static final String MODULE_NAME = "RNZendesk";

    public RNZendeskBridge(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    private ReadableMap zendeskInstance;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void initializeInstance(ReadableMap config) {
        Zendesk.INSTANCE.init(getReactApplicationContext(), config.getString("zendeskUrl"), config.getString("appId"), config.getString("clientId"));
        Support.INSTANCE.init(Zendesk.INSTANCE);

        return Zendesk.INSTANCE;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void initializeAuth(ReadableMap config) {
        this.zendeskInstance = initializeInstance(config);
        
        identifyJWT(config.getString("userId"))

        registerPushToken(config.getString("deviceToken"))
    }

    // MARK: - Indentification

    @ReactMethod
    public void identifyJWT(String token) {
        JwtIdentity identity = new JwtIdentity(token);
        this.zendeskInstance.setIdentity(identity);
    }

    @ReactMethod
    public void identifyAnonymous(String name, String email) {
        Identity identity = new AnonymousIdentity.Builder()
            .withNameIdentifier(name)
            .withEmailIdentifier(email)
            .build();

        this.zendeskInstance.setIdentity(identity);
    }

    // MARK: - UI Methods

    @ReactMethod
    public void showHelpCenter(ReadableMap options) {
//        Boolean hideContact = options.getBoolean("hideContactUs") || false;
        UiConfig hcConfig = HelpCenterActivity.builder()
                .withContactUsButtonVisible(!(options.hasKey("hideContactSupport") && options.getBoolean("hideContactSupport")))
                .config();

        Intent intent = HelpCenterActivity.builder()
                .withContactUsButtonVisible(true)
                .intent(getReactApplicationContext(), hcConfig);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(intent);
    }
    
    @ReactMethod
    public void showNewTicket(ReadableMap options) {
        ArrayList tags = options.getArray("tags").toArrayList();

        Intent intent = RequestActivity.builder()
                .withTags(tags)
                .intent(getReactApplicationContext());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(intent);
    }

    @ReactMethod
    public void showTicketList() {
        Intent intent = RequestListActivity.builder()
                .intent(getReactApplicationContext());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(intent);
    }

    @ReactMethod
    public void unregisterPushToken(ReadableMap config) {
        if(this.zendeskInstance == null) {
            this.zendeskInstance = initializeInstance(config);
        }
        this.zendeskInstance.provider().pushRegistrationProvider().unregisterDevice(new ZendeskCallback<Void>() {
            @Override
            public boolean onSuccess(final Void response) { 
                return true;
            }

            @Override
            public boolean onError(ErrorResponse errorResponse) { 
                return  false;
            }
        });
    }

    @ReactMethod
    public void registerPushToken(String token) {
       this.zendeskInstance.provider().pushRegistrationProvider().registerWithDeviceIdentifier(token, new ZendeskCallback<String>() {
            @Override
            public boolean onSuccess(String result) {
                return true;
             }

            @Override
            public boolean onError(ErrorResponse errorResponse) { 
               return false;
            }
       });
    }
}
