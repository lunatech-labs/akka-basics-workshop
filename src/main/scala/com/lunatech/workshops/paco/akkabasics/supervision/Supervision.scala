package com.lunatech.workshops.paco.akkabasics.supervision

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object Supervision extends App {
  override def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("supervision")
    val supervisor = actorSystem.actorOf(Props[Supervisor])

    StdIn.readLine

    actorSystem.terminate
  }
}
