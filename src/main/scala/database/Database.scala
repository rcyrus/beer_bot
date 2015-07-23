package database

import listeners.persistence.TapListing
import sorm.{Entity, InitMode, Instance}

/**
 * Database ORM primary class
 */
object Database extends Instance (
  entities = Set(
    //TODO: Any object class you want to persist will need to be listed here in an Entity object
    Entity[TapListing]()
  ),
  url = "jdbc:h2:./beerbot",
  user = "",
  password = "",
  initMode = InitMode.Create
)
