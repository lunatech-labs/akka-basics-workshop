package com.lunatech.workshops.paco.akkabasics.persistence

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.lunatech.workshops.paco.akkabasics.persistence.Storage.{Add, All}

import scala.concurrent.duration.DurationInt
import scala.io.StdIn

object Persistence extends App {
  override def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("persistence")
    val persistentActor = actorSystem.actorOf(Props[Storage])

    implicit val timeout = Timeout(1.second)

    {
      import actorSystem.dispatcher

      persistentActor ! Add("bar")
      persistentActor ! Add("foo")
      (persistentActor ? All).foreach(msg => println(Console.MAGENTA + s"$msg" + Console.RESET))

    }

    StdIn.readLine

    actorSystem.terminate

    StdIn.readLine

    val actorSystem2 = ActorSystem("persistence2")

    {
      import actorSystem2.dispatcher

      val persistentActor2 = actorSystem2.actorOf(Props[Storage])
      (persistentActor2 ? All).foreach(msg => println(Console.MAGENTA + s"$msg" + Console.RESET))
    }

    StdIn.readLine
    actorSystem2.terminate
  }
}
