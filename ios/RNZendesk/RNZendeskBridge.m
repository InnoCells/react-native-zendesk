#import "RNZendeskBridge.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_REMAP_MODULE(RNZendesk, RNZendesk, NSObject)

RCT_EXTERN_METHOD(initialize:(NSDictionary *)config);
RCT_EXTERN_METHOD(identifyUser:(NSString)user);

RCT_EXTERN_METHOD(showHelpCenter);

RCT_EXTERN_METHOD(registerPushToken:(NSString)token);
RCT_EXTERN_METHOD(unregisterPushToken);

RCT_EXTERN_METHOD(isInitialized);

@end
