package com.lunatech.workshops.paco.akkabasics.persistence

import akka.persistence.PersistentActor
import com.lunatech.workshops.paco.akkabasics.persistence.Storage._

class Storage extends PersistentActor {
//  import context.dispatcher

  var set = Set[String]()

  override def persistenceId: String = "storage-1"

  override def receiveRecover: Receive = {
    case m: Message =>
      updateState(m)
  }

  override def receiveCommand: Receive = {
    case m: Message =>
      persist(m)(updateState)
  }

  private def updateState(msg: Message): Unit = msg match {
    case Add(value) =>
      set += value
      sender ! ()
    case Get(value) =>
      sender ! set(value)
    case Remove(value) =>
      set -= value
      sender ! ()
    case All =>
      sender ! set
  }
}

object Storage {
  sealed trait Message

  case class Add(value: String) extends Message
  case class Get(value: String) extends Message
  case class Remove(value: String) extends Message
  case object All extends Message
}


