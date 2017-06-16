package com.lunatech.workshops.paco.akkabasics.alods

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object GuaranteedDelivery extends App {
  override def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("guaranteed-delivery")

    val flakyReceiver = actorSystem.actorOf(FlakyReceiver.props)
    val producer = actorSystem.actorOf(Producer.props(flakyReceiver))

    StdIn.readLine()

    actorSystem.terminate()
  }
}
