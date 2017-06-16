package com.lunatech.workshops.paco.akkabasics.typesafe.setactor

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.lunatech.workshops.paco.akkabasics.typesafe.setactor.SetActor._

import scala.concurrent.Future
import scala.reflect.ClassTag

private class SetActor extends Actor {
  var set = Set[String]()

  override def receive: Receive = {
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

private object SetActor {
  sealed trait Message[R]

  case class Add(value: String) extends Message[Unit]
  case class Get(value: String) extends Message[Boolean]
  case class Remove(value: String) extends Message[Unit]
  case object All extends Message[Set[String]]
}

class SetActorAdapter private (setActor: ActorRef) {
  private def sendMessage[R : ClassTag](message: Message[R])(implicit timeout: Timeout): Future[R] = (setActor ? message).mapTo[R]

  def add(value: String)(implicit timeout: Timeout): Future[Unit] = sendMessage(Add(value))
  def get(value: String)(implicit timeout: Timeout): Future[Boolean] = sendMessage(Get(value))
  def remove(value: String)(implicit timeout: Timeout): Future[Unit] = sendMessage(Remove(value))
  def all(implicit timeout: Timeout): Future[Set[String]] = sendMessage(All)
}

object SetActorAdapter {
  def apply(f: (Props, String) => ActorRef): SetActorAdapter = {
    new SetActorAdapter(f(Props(new SetActor), "child"))
  }
}


