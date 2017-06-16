package com.lunatech.workshops.paco.akkabasics.alods

import akka.actor.{Actor, ActorRef, Props}
import com.lunatech.workshops.paco.akkabasics.alods.FlakyReceiver.Message
import com.lunatech.workshops.paco.akkabasics.alods.Producer.Ping
import akka.pattern.pipe

import scala.concurrent.Future
import scala.concurrent.duration.DurationDouble

class Producer(receiver: ActorRef) extends Actor {
  import context.dispatcher

  private def sendPing(sequence: Int): Unit =
    context.system.scheduler.scheduleOnce(0.5.seconds, self, Ping(sequence))

  override def preStart(): Unit = {
    sendPing(1)
  }

  override def receive: Receive = {
    case Ping(count) =>
      Future.successful(1).pipeTo(self)
      receiver ! Message(count)
      sendPing(count + 1)
  }
}

object Producer {
  def props(receiver: ActorRef) = Props(new Producer(receiver))

  private case class Ping(sequence: Int)
}
