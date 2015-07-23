package listeners

import definition.MessageListener
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Display the help text for all beer bot listeners
 */
class HelpListener extends MessageListener{
  override var slackClient: SlackRtmClient = _

  var listeners: List[MessageListener] = List()
  private var sourceMessage: Message = null

  override def showHelp(): String = {
    "*Beerbot Help* \n|| Usage: \"@beerbot help\" \n|| Shows you all of the cool features Beerbot has to offer!"
  }

  override def isAMatch(message: Message): Boolean = {
    sourceMessage = message
    val messageParts = message.text.split(" ").toList

    messageParts.nonEmpty && messageParts.length > 1 && messageParts(1).trim.toLowerCase.equals("help")
  }

  /**
   * Process the message that is a match for this listener
   */
  override def processMessage(msgText: String): Unit = {
    listeners.foreach { listener =>
      respond(listener.showHelp() + "\n", sourceMessage)
    }
  }
}
