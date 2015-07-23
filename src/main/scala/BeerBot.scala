import akka.actor.ActorSystem
import listeners.{BeerInfoSearch, HelpListener}
import slack.rtm.SlackRtmClient

object BeerBot {
  //TODO: Would be nice to not have to instantiate each listener manually, but instead auto-load classes
  val listeners = List(
    new HelpListener,
    new BeerInfoSearch
  )

  def main(args: Array[String]): Unit = {
    val token = "xoxb-7413056195-71TJcdoFTjTIEJ6ghpvpr9wu"

    implicit val system = ActorSystem("slack")
    implicit val ec = system.dispatcher
    val rtmClient = SlackRtmClient(token)

    //Register all the supplied listeners with the Slack client
    listeners.foreach(listen => listen.registerHandler(rtmClient))

    //TODO: There has got to be a better way to do this; it's kinda clunky!
    listeners.head match {
      case item: HelpListener => item.listeners = listeners
    }
  }
}


