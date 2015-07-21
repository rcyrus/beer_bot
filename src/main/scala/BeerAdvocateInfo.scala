import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

/**
 *
 */
class BeerAdvocateInfo {

  def getBeerInfo(searchTerm: String): Unit = {
    val searchTerm = "Summit+Extra+Pale+Ale"

    val browser = new Browser
    val docContents = browser.get("http://www.beeradvocate.com/search/?q=" + searchTerm + "&qt=beer")

    val foundItems = docContents >> extractor("#ba-content > div", elementList) >> extractor("a", elementList) filter { list => list.nonEmpty }

    if (foundItems.nonEmpty)
      foundItems.head foreach println
    else
      println("Sorry no results")
  }

}
