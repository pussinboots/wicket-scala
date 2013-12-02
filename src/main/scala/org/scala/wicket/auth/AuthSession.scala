package org.scala.wicket.auth

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.request.Request


class AuthSession(request: Request) extends AuthenticatedWebSession(request) {
  val WICKET: String = "wicket"

  def authenticate(username: String, password: String): Boolean = WICKET == username && WICKET == password

  def getRoles: Roles = {
    if (isSignedIn) {
      return new Roles(Roles.ADMIN)
    }
    return null
  }
}
