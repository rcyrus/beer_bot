package definition

import slack.SlackUtil
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Defines the contract for all classes that listen and respond to clack messages
 */
trait MessageListener {
  var slackClient: SlackRtmClient

  def showHelp(): String = "TODO: Supply help information"

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

  def respond(response: String, sourceMsg: Message): Unit = {
    slackClient.sendMessage(sourceMsg.channel, response)
  }

  def isAMatch(message: Message): Boolean = false

  def processMessage(msgText: String): Unit
}
