//
//  RNZendesk.swift
//  RNZendesk
//
//  Created by David Chavez on 24.04.18.
//  Copyright © 2018 David Chavez. All rights reserved.
//

import UIKit
import Foundation
import SupportSDK
import ZendeskCoreSDK
import CommonUISDK

@objc(RNZendesk)
class RNZendesk: RCTEventEmitter {

    override public static func requiresMainQueueSetup() -> Bool {
        return false;
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
        guard
            let appId = config["appId"] as? String,
            let clientId = config["clientId"] as? String,
            let zendeskUrl = config["zendeskUrl"] as? String,
            let userId = config["userId"] as? String else { return }
        
        Zendesk.initialize(appId: appId, clientId: clientId, zendeskUrl: zendeskUrl)
        Support.initialize(withZendesk: Zendesk.instance)

        let identity = Identity.createJwt(token: userId)
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
        if Zendesk.instance != nil {
            ZDKPushProvider(zendesk: Zendesk.instance!).unregisterForPush();
        }
    }

    @objc(registerPushToken:)
    func registerPushToken(token: String) {
        let locale = NSLocale.preferredLanguages.first ?? "en";
        ZDKPushProvider(zendesk: Zendesk.instance!).register(deviceIdentifier: token, locale: locale) { (pushResponse, error) in
           
        };
    }
}
