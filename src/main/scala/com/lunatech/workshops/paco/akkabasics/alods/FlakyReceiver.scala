package com.lunatech.workshops.paco.akkabasics.alods

import akka.actor.{Actor, Props}
import com.lunatech.workshops.paco.akkabasics.alods.FlakyReceiver.Message

import scala.util.Random

class FlakyReceiver extends Actor {
  override def receive: Receive = {
    case Message(i) if Random.nextBoolean => println(s"Received message #$i")
  }
}

object FlakyReceiver {
  def props = Props(new FlakyReceiver)

  case class Message(i: Int)
}
