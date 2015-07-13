import slack.rtm.SlackRtmClient
import slack.SlackUtil
import akka.actor.ActorSystem

object BeerBot{
  def main(args: Array[String]): Unit = {
    val token = "xoxb-7413056195-DvEweSeRHOzTO7qZhHvxBfAy"
    implicit val system = ActorSystem("slack")
    implicit val ec = system.dispatcher

    val client = SlackRtmClient(token)
    val selfId = client.state.self.id

    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)

      if(mentionedIds.contains(selfId)) {
        client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
      }
    }
  }
}


