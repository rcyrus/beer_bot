import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

/**
 *
 */
class BeerInfoSearch {
  private var baseUrl = "http://www.beeradvocate.com"

  def getBeerInfo(searchTerm: String): Unit = {
    val searchTerm = "Summit+Extra+Pale+Ale"

    val browser = new Browser
    val searchPageContents = browser.get(baseUrl + "/search/?q=" + searchTerm + "&qt=beer")

    val initSearchResults = searchPageContents >> extractor("#ba-content > div", elementList) >> extractor("a", elementList) filter { list => list.nonEmpty }

    if (initSearchResults.nonEmpty && initSearchResults.head.length > 1) {
      val beerInfoElement = initSearchResults.head.head
      val beerUrl = beerInfoElement.attr("href")

      val beerPageContents = browser.get(baseUrl + beerUrl)

      val baRatingEl = beerPageContents >> extractor(".ba-score", element)
      val styleElements = beerPageContents >> extractor("a", elementList) filter { el => el.attr("href").contains("/beer/style") }

      println("BA Rating: " + baRatingEl.html())
      println("Style: " + styleElements.head.getElementsByTag("b").head.html())
    } else
      println("Sorry I couldn't find any results for that beer. Try to be more specific.")
  }

}
