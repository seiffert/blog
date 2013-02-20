package models
  
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.Clob
import org.mindrot.jbcrypt.BCrypt

case class Admin(id: Int, username: String, password: String, permission: AdminPermission)

object Admin {
  
  def findById(id: Int) = {
    var admin: Admin = null
    if (id == current.configuration.getInt("admin.id").getOrElse(0)) {
        admin = Admin(id, current.configuration.getString("admin.username") getOrElse(""), current.configuration.getString("admin.password") getOrElse(""), Administrator)
    }
    
    Option(admin)
  }

  def authenticate(username: String, password: String): Option[Admin] = {
    var admin : Admin = null
    
    if (current.configuration.getString("admin.username").getOrElse("") == username
        && current.configuration.getString("admin.password").getOrElse("") == password) {
          admin = Admin(current.configuration.getInt("admin.id").getOrElse(0), username, password, Administrator)
    }
    
    Option(admin)
  }
}