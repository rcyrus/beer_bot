import listeners.BeerInfoSearch

object BeerBot {
  def main(args: Array[String]): Unit = {
//    val token = "xoxb-7413056195-71TJcdoFTjTIEJ6ghpvpr9wu"
//    implicit val system = ActorSystem("slack")
//    implicit val ec = system.dispatcher
//
//    val client = SlackRtmClient(token)
//    val selfId = client.state.self.id
//
//    client.onMessage { message =>
//      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
//
//      if(mentionedIds.contains(selfId)) {
//        client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
//      }
//    }

    val baInfo = new BeerInfoSearch
    //baInfo.getBeerInfo("Summit Extra Pale Ale")
    baInfo.getBeerInfo("Deschutes Chainbreaker")
  }
}


