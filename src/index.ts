import { NativeModules } from 'react-native'

const { RNZendesk } = NativeModules

interface Config {
  appId: string
  clientId: string
  zendeskUrl: string
}

export function initialize(config: Config) {
  RNZendesk.initialize(config)
}

export function showHelpCenter() {
  RNZendesk.showHelpCenter()
}

export function unregisterPushToken() {
  return RNZendesk.unregisterPushToken()
}

export function registerPushToken(deviceToken: string) {
  return RNZendesk.registerPushToken(deviceToken)
}

export function identifyUser(user: string) {
  return RNZendesk.identifyUser(user)
}

export function isInitialized(): boolean {
  return RNZendesk.isInitialized()
}
