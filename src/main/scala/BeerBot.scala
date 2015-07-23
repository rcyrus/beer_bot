import akka.actor.ActorSystem
import listeners.BeerInfoSearch
import slack.rtm.SlackRtmClient

object BeerBot {
  //TODO: Would be nice to not have to instantiate each listener manually, but instead auto-load classes
  val listeners = Array(
    new BeerInfoSearch
  )

  def main(args: Array[String]): Unit = {
    val token = "xoxb-7413056195-71TJcdoFTjTIEJ6ghpvpr9wu"

    implicit val system = ActorSystem("slack")
    implicit val ec = system.dispatcher
    val rtmClient = SlackRtmClient(token)

    //Register all the supplied listeners with the Slack client
    listeners.foreach(listen => listen.registerHandler(rtmClient))
  }
}


