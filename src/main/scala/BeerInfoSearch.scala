import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import org.jsoup.nodes.Document

/**
 *
 */
class BeerInfoSearch {
  private var baseUrl = "http://www.beeradvocate.com"
  private var browserParser:Browser = new Browser

  def getBeerInfo(searchTerm: String): Unit = {
    val searchPageContents = browserParser.get(baseUrl + "/search/?q=" + createSearchString(searchTerm) + "&qt=beer")

    val initSearchResults = searchPageContents >> extractor("#ba-content > div", elementList) >> extractor("a", elementList) filter { list => list.nonEmpty }

    if (initSearchResults.nonEmpty && initSearchResults.head.length > 1) {
      val beerInfoElement = initSearchResults.head.head
      val beerUrl = beerInfoElement.attr("href")

      val beerPageContents = browserParser.get(baseUrl + beerUrl)

      val styleElements = beerPageContents >> extractor("a", elementList) filter { el => el.attr("href").contains("/beer/style") }

      var abvSource = styleElements.head.parent.getElementsContainingText("ABV").last.previousSibling.outerHtml()
      abvSource = abvSource.replace("| &nbsp;", "").trim()

      println("BA Rating: " + getBaRating(beerPageContents))
      println("Style: " + styleElements.head.getElementsByTag("b").head.html())
      println("ABV: " + abvSource)
    } else {
      println("Sorry I couldn't find any results for that beer. Try to be more specific.")
    }
  }

  def createSearchString(source: String): String = {
    source.trim.replace(" ", "+")
  }

  def getBaRating(pageContents: Document): String = {
    val ratingEl = pageContents >> extractor(".ba-score", element)
    ratingEl.html
  }

}
