import akka.actor.ActorSystem
import listeners.BeerInfoSearch
import slack.rtm.SlackRtmClient

object BeerBot {
  def main(args: Array[String]): Unit = {
    val token = "xoxb-7413056195-71TJcdoFTjTIEJ6ghpvpr9wu"
    implicit val system = ActorSystem("slack")
    implicit val ec = system.dispatcher

    val rtmClient = SlackRtmClient(token)

//    client.onMessage { message =>
//      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
//
//      if(mentionedIds.contains(selfId)) {
//        client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
//      }
//    }

    val baListener = new BeerInfoSearch
    baListener.registerHandler(rtmClient)
  }
}


