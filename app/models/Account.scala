package models
  
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.Clob
import org.mindrot.jbcrypt.BCrypt

case class Account(id: String, username: String, password: String, name: String, permission: Permission)

object Account {

  object Clob {
    def unapply(clob: Clob): Option[String] = Some(clob.getSubString(1, clob.length.toInt))
  }

  implicit val rowToPermission: Column[Permission] = {
    Column.nonNull[Permission] { (value, meta) =>
      value match {
        case "Administrator" => Right(Administrator)
        case Clob("Administrator") => Right(Administrator)
        case "NormalUser" => Right(NormalUser)
        case Clob("NormalUser") => Right(NormalUser)
        case _ => Left(TypeDoesNotMatch(
          "Cannot convert %s : %s to Permission for column %s".format(value, value.getClass, meta.column)))
      }
    }
  }

  val simple = {
    get[String]("account.id") ~
    get[String]("account.username") ~
    get[String]("account.password") ~
    get[String]("account.name") ~
    get[Permission]("account.permission") map {
      case id~username~pass~name~perm => Account(id, username, pass, name, perm)
    }
  }

  def authenticate(username: String, password: String): Option[Account] = {
    findOneByUsername(username).filter { account => BCrypt.checkpw(password, account.password) }
  }

  def findOneByUsername(username: String): Option[Account] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM account WHERE username = {username}").on(
        'username -> username
      ).as(simple.singleOpt)
    }
  }

  def findOneById(id: String): Option[Account] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM account WHERE id = {id}").on(
        'id -> id
      ).as(simple.singleOpt)
    }
  }

  def findAll: Seq[Account] = {
    DB.withConnection { implicit connection =>
      SQL("select * from account").as(simple.*)
    }
  }

  def create(account: Account) {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO account VALUES ({id}, {username}, {pass}, {name}, {permission})").on(
        'id -> account.id,
        'username -> account.username,
        'pass -> BCrypt.hashpw(account.password, BCrypt.gensalt()),
        'name -> account.name,
        'permission -> account.permission.toString
      ).executeUpdate()
    }
  }


}