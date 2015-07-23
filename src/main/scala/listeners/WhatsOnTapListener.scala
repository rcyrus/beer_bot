package listeners

import database.Database
import definition.MessageListener
import listeners.WhatsOnTapState.WhatsOnTapState
import listeners.persistence.TapListing
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Allows users to ask what's currently on tap and admins to set what is on tap
 */
class WhatsOnTapListener extends MessageListener {
  override var slackClient: SlackRtmClient = _

  protected var sourceMessage: Message = null
  protected var state: WhatsOnTapState = WhatsOnTapState.NOTHING

  override def isAMatch(message: Message): Boolean = {
    sourceMessage = message

    message.text.toLowerCase match {
      case text if text.contains("on-tap") => {
        state = WhatsOnTapState.AKSING
        true
      }
      case text if text.contains("request") => {
        state = WhatsOnTapState.REQUESTING
        true
      }
      case _ => false
    }
  }

  /**
   * Process the message that is a match for this listener
   */
  override def processMessage(msgText: String): Unit = {
    state match {
      case st if st == WhatsOnTapState.AKSING => respondWithTapListing()
      case st if st == WhatsOnTapState.REQUESTING => requestBeer()
    }

    state = WhatsOnTapState.NOTHING
  }

  protected def respondWithTapListing() = {
    val listings = Database.query[TapListing].fetch()
    if (listings.nonEmpty) {

    } else {
      respond("No beers have been set as on tap just yet. Check back soon!", sourceMessage)
    }
  }

  protected def requestBeer() = {
    respond("Thanks so much for the request!", sourceMessage)
  }
}

object WhatsOnTapState extends Enumeration {
  type WhatsOnTapState = Value

  val NOTHING,
      AKSING,
      REQUESTING,
      SETTING = Value
}