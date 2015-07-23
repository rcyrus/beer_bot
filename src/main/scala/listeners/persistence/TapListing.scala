package listeners.persistence

/**
 * A beer that been set as on tap
 */
case class TapListing(name: String, requestingUser: String, beerAdvocateId: Int) { }