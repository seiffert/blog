package models

sealed trait AdminPermission

case object Administrator extends AdminPermission