package com.lunatech.workshops.paco.akkabasics

import akka.actor.Actor
import akka.pattern.ask
import akka.pattern.pipe
import com.lunatech.workshops.paco.akkabasics.MyActor.{GetNames, Message, Name}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class MyActor extends Actor {
  var names = Set.empty[String]

  import context.dispatcher

  override def receive: Receive = {
    case m: Message => m match {
      case GetNames =>
        sender ! names

      case Name(name) =>
        names = names + name
        sender ! s"Hello, $name!"

    }
    case _ =>
      println("WAT?")
  }
}

object MyActor {
  sealed trait Message

  final case class Name(s: String) extends Message
  final case object GetNames extends Message
}
