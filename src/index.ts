import { NativeModules } from 'react-native'

const { RNZendesk } = NativeModules

interface Config {
  appId: string
  clientId: string
  zendeskUrl: string
  userId: string
  deviceToken: string
}

// MARK: - Initialization

export function initialize(config: Config) {
  RNZendesk.initializeAuth(config)
}

// MARK: - Indentification

export function identifyJWT(token: string) {
  RNZendesk.identifyJWT(token)
}

export function identifyAnonymous(name?: string, email?: string) {
  RNZendesk.identifyAnonymous(name, email)
}

// MARK: - UI Methods

interface HelpCenterOptions {
  hideContactSupport?: boolean
}

export function showHelpCenter(options: HelpCenterOptions = { hideContactSupport: false }) {
  RNZendesk.showHelpCenter(options)
}

interface NewTicketOptions {
  tags?: string[]
}

export function showNewTicket(options: NewTicketOptions) {
  RNZendesk.showNewTicket(options)
}

export function showTicketList() {
  RNZendesk.showTicketList()
}

export function unregisterPushToken(config: { appId: string; clientId: string; zendeskUrl: string }) {
  return RNZendesk.unregisterPushToken(config)
}

export function registerPushToken(deviceToken: string) {
  return RNZendesk.registerPushToken(deviceToken)
}
