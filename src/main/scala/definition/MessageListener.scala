package definition

import slack.SlackUtil
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Defines the contract for all classes that listen and respond to clack messages
 */
trait MessageListener {
  var slackClient: SlackRtmClient

  /**
   * Return some help text for the particular listener
   */
  def showHelp(): String = "TODO: Supply help information"

  /**
   * Register this listener with beer bot and get a reference to the Slack client
   */
  def registerHandler(client: SlackRtmClient): Unit = {
    slackClient = client

    slackClient.onMessage { message =>
      val selfId = client.state.self.id
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)

      if (mentionedIds.contains(selfId) && isAMatch(message))  {
        processMessage(message.text)
      }
    }
  }

  /**
   * Say something back into the channel where the command originated
   */
  def respond(response: String, sourceMsg: Message): Unit = {
    slackClient.sendMessage(sourceMsg.channel, response)
  }

  /**
   * Whether or not the particular message is a match for this listener
   */
  def isAMatch(message: Message): Boolean = false

  /**
   * Process the message that is a match for this listener
   */
  def processMessage(msgText: String): Unit
}
