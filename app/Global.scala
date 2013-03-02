import play.api._

import models._
import anorm._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    if (Account.findAll.isEmpty) {
      Account.create(
        Account("1", "paul", "test", "Paul Seiffert", Administrator)
      )
    }
  }
}