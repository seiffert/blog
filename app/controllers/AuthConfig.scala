package controllers

import jp.t2v.lab.play20.auth.{AuthConfig => BaseAuthConfig, IdContainer, CookieIdContainer}
import scala.reflect.{ClassTag, classTag}
import models.{Account, Permission, Administrator, NormalUser}
import play.api.mvc._
import play.api.mvc.Results._

trait AuthConfig extends BaseAuthConfig {

  /**
   * A type that is used to identify a user.
   * `String`, `Int`, `Long` and so on.
   */
  type Id = String

  /**
   * A type that represents a user in your application.
   * `User`, `Account` and so on.
   */
  type User = Account

  /**
   * A type that is defined by every action for authorization.
   * This sample uses the following trait:
   *
   * sealed trait Permission
   * case object Administrator extends Permission
   * case object NormalUser extends Permission
   */
  type Authority = Permission

  /**
   * A `ClassManifest` is used to retrieve an id from the Cache API.
   * Use something like this:
   */
  val idTag: ClassTag[Id] = classTag[Id]
  // for version 0.5 as follows
  // val idManifest: ClassManifest[Id] = classManifest[Id]

  /**
   * The session timeout in seconds
   */
  val sessionTimeoutInSeconds: Int = 3600

  /**
   * A function that returns a `User` object from an `Id`.
   * You can alter the procedure to suit your application.
   */
  def resolveUser(id: Id): Option[Account] = Account.findById(id)

  /**
   * Where to redirect the user after a successful login.
   */
  def loginSucceeded(request: RequestHeader) = Redirect(routes.BlogPosts.index)

  /**
   * Where to redirect the user after logging out
   */
  def logoutSucceeded(request: RequestHeader) = Redirect(routes.BlogPosts.index)

  /**
   * If the user is not logged in and tries to access a protected resource then redirct them as follows:
   */
  def authenticationFailed(request: RequestHeader) = Redirect(routes.Auth.index)

  /**
   * If authorization failed (usually incorrect password) redirect the user as follows:
   */
  def authorizationFailed(request: RequestHeader) = Forbidden("no permission")

  /**
   * A function that determines what `Authority` a user has.
   * You should alter this procedure to suit your application.
   */
  def authorize(user: User, authority: Authority): Boolean =
    (user.permission, authority) match {
      case (Administrator, _) => true
      case (NormalUser, _) => true
      case _ => false
    }

  /**
   * Whether use the secure option or not use it in the cookie.
   * However default is false, I strongly recommend using true in a production.
   */
  override lazy val cookieSecureOption: Boolean = play.api.Play.current.configuration.getBoolean("auth.cookie.secure").getOrElse(true)

  override lazy val idContainer: IdContainer[Id] = new CookieIdContainer[Id]
}