import UIKit
import Foundation
import SupportSDK
import ZendeskCoreSDK
import CommonUISDK

@objc(RNZendesk)
class RNZendesk: RCTEventEmitter {

    override public static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(constantsToExport)
    override func constantsToExport() -> [AnyHashable: Any] {
        return [:]
    }
    
    @objc(supportedEvents)
    override func supportedEvents() -> [String] {
        return []
    }

    @objc(initialize:)
    func initialize(config: [String: Any]) -> Void {
        Zendesk.initialize(appId: config["appId"] as! String, clientId: config["clientId"] as! String, zendeskUrl: config["zendeskUrl"] as! String)
        Support.initialize(withZendesk: Zendesk.instance)
    }

    @objc(identifyUser:)
    func identifyUser(user: String) {
        let identity = Identity.createJwt(token: user)
        Zendesk.instance?.setIdentity(identity)
    }
    
    @objc(showHelpCenter)
    func showHelpCenter() {
        DispatchQueue.main.async {
            let helpCenter = HelpCenterUi.buildHelpCenterOverviewUi(withConfigs: [])
            
            let nvc = UINavigationController(rootViewController: helpCenter)
            UIApplication.shared.keyWindow?.rootViewController?.present(nvc, animated: true)
        }
    }

    @objc(unregisterPushToken)
    func unregisterPushToken() {
        ZDKPushProvider(zendesk: Zendesk.instance!).unregisterForPush();
    }

    @objc(registerPushToken:)
    func registerPushToken(token: String) {
        let locale = NSLocale.preferredLanguages.first ?? "en";
        ZDKPushProvider(zendesk: Zendesk.instance!).register(deviceIdentifier: token, locale: locale) { (pushResponse, error) in }
    }

    @objc(isInitialized)
    func isInitialized() -> Bool {
        return Zendesk.instance != nil
    }
}
