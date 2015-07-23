package listeners

import definition.MessageListener
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Allows users to ask what's currently on tap and admins to set what is on tap
 */
class WhatsOnTapListener extends MessageListener {
  override var slackClient: SlackRtmClient = _

  protected var sourceMessage: Message = null

  override def isAMatch(message: Message): Boolean = {
    sourceMessage = message

    message.text.toLowerCase.contains("on-tap")
  }

  /**
   * Process the message that is a match for this listener
   */
  override def processMessage(msgText: String): Unit = {
    respond("No beers have been set as on tap just yet. Check back soon!", sourceMessage)
  }
}
