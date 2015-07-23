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

  /**
   * TODO: There will need to be a way for the listeners to know if the current user is "admin"
   * Currently just checking for Andrew
   */
  def isAdminUser(sourceMsg: Message): Boolean = sourceMsg.user.equals("U046S2MCY")

  override def showHelp(): String = {
    "*What's On Tap?* \n|| Usage: \"@beerbot on-tap\" \n|| Shows what beers are currently on tap in the kegerator."
  }

  override def isAMatch(message: Message): Boolean = {
    sourceMessage = message

    message.text.toLowerCase match {
      case text if text.contains("on-tap") =>
        state = WhatsOnTapState.AKSING
        true
      case text if text.contains("set-tap") && isAdminUser(message) =>
        state = WhatsOnTapState.SETTING
        true
      case _ => false
    }
  }

  /**
   * Process the message that is a match for this listener
   */
  override def processMessage(msgText: String): Unit = {
    state match {
      case st if st == WhatsOnTapState.AKSING => respondWithTapListing()
      case st if st == WhatsOnTapState.SETTING => setBeerOnTap()
    }

    state = WhatsOnTapState.NOTHING
  }

  /**
   * Query the set of stored beers and respond back to the user
   */
  protected def respondWithTapListing() = {
    val listings = Database.query[TapListing].fetch()
    if (listings.nonEmpty) {
      var outString = "Here's What's on Tap!"
      listings.foreach(listing => outString += "\n|| *" + listing.name.trim + "*")

      respond(outString, sourceMessage)
    } else {
      respond("No beers have been set as on tap just yet. Check back soon!", sourceMessage)
    }
  }

  /**
   * Set a beer as on tap
   *
   * TODO: Possibly go and get BeerAdvocate ID
   * TODO: Update value object to be more descriptive of data
   * TODO: Make sure ony two beers can be set at any given time
   */
  protected def setBeerOnTap() = {
    val beerSet = sourceMessage.text.split(" ").toList.drop(2).mkString("", " ", "")

    if (beerSet.nonEmpty) {
      val newTap = TapListing(beerSet, "", -1)
      Database.save(newTap)

      respond("You have successfully added to the on-tap list", sourceMessage)
    }
  }
}

object WhatsOnTapState extends Enumeration {
  type WhatsOnTapState = Value

  val NOTHING,
      AKSING,
      SETTING = Value
}