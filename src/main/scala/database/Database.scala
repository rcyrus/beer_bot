package database

import listeners.persistence.Location
import sorm.{Entity, InitMode, Instance}

/**
 *
 */
object Database extends Instance (
  entities = Set(
    Entity[Location]()
  ),
  url = "jdbc:h2:mem:test",
  user = "",
  password = "",
  initMode = InitMode.Create
)
