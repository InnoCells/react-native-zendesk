import { NativeModules } from 'react-native'

const { RNZendesk } = NativeModules

interface Config {
  appId: string
  clientId: string
  zendeskUrl: string
  userId: string
  deviceToken: string
}

export function initialize(config: Config) {
  RNZendesk.initialize(config)
}

interface HelpCenterOptions {
  hideContactSupport?: boolean
}

export function showHelpCenter(options: HelpCenterOptions = { hideContactSupport: false }) {
  RNZendesk.showHelpCenter(options)
}

export function unregisterPushToken() {
  return RNZendesk.unregisterPushToken()
}
