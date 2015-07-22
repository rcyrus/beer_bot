package definition

import slack.rtm.SlackRtmClient

/**
 * Defines the contract for all classes that listen and respond to clack messages
 */
trait MessageListener {
  def showHelp(): String
  def registerHandler(client: SlackRtmClient): Unit
  def respond(): Unit
}
