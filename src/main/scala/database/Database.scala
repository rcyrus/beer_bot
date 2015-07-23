package database

import sorm.{Entity, InitMode, Instance}

/**
 * Database ORM primary class
 */
object Database extends Instance (
  entities = Set(), //TODO: Any object class you want to persist will need to be listed here
  url = "jdbc:h2:./beerbot",
  user = "",
  password = "",
  initMode = InitMode.Create
)
