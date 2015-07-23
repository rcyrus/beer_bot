package listeners

import definition.MessageListener
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import org.jsoup.nodes.Document
import slack.models.Message
import slack.rtm.SlackRtmClient

/**
 * Beer Bot feature that allows users to find out information about a beer
 */
class BeerInfoSearch extends MessageListener {
  override var slackClient: SlackRtmClient = _

  private var baseUrl = "http://www.beeradvocate.com"
  private var browserParser:Browser = new Browser
  private var sourceMessage: Message = null

  override def showHelp(): String = {
    "Beer Info: @beerbot info <beer name>"
  }

  override def isAMatch(message: Message): Boolean = {
    sourceMessage = message
    val messageParts = message.text.split(" ").toList

    messageParts.nonEmpty && messageParts.length > 1 && messageParts(1).trim.equals("info")
  }

  override def processMessage(msgText: String): Unit = {
    val result = msgText.split(" ").toList.drop(2).mkString("", " ", "")

    searchBeerInfo(result)
  }

  private def searchBeerInfo(searchTerm: String): Unit = {
    val searchPageContents = browserParser.get(baseUrl + "/search/?q=" + createSearchString(searchTerm) + "&qt=beer")

    val initSearchResults = searchPageContents >> extractor("#ba-content > div", elementList) >> extractor("a", elementList) filter { list => list.nonEmpty }

    if (initSearchResults.nonEmpty && initSearchResults.head.length > 1) {
      val beerInfoElement = initSearchResults.head.head
      val beerUrl = beerInfoElement.attr("href")

      val fullBeerPageUrl = baseUrl + beerUrl
      val beerPageContents = browserParser.get(fullBeerPageUrl)

      val styleElements = beerPageContents >> extractor("a", elementList) filter { el => el.attr("href").contains("/beer/style") }

      var abvSource = styleElements.head.parent.getElementsContainingText("ABV").last.previousSibling.outerHtml()
      abvSource = abvSource.replace("| &nbsp;", "").trim()

      var output = searchTerm + ":"
      output += "\nBA Rating: " + getBaRating(beerPageContents)
      output += "\nStyle: " + styleElements.head.getElementsByTag("b").head.html()
      output += "\nABV: " + abvSource
      output += "\n" + fullBeerPageUrl

      respond(output, sourceMessage)
    } else {
      respond("Sorry I couldn't find any results for that beer.\nBe sure to type out the full name of the beer without abbreviations.", sourceMessage)
    }
  }

  private def createSearchString(source: String): String = {
    source.trim.replace(" ", "+")
  }

  private def getBaRating(pageContents: Document): String = {
    val ratingEl = pageContents >> extractor(".ba-score", element)
    ratingEl.html
  }

}
