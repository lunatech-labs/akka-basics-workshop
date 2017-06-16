package com.lunatech.workshops.paco.akkabasics.typesafe

import akka.actor.ActorSystem
import akka.util.Timeout
import com.lunatech.workshops.paco.akkabasics.typesafe.setactor.SetActorAdapter

import scala.concurrent.duration.DurationInt

object Typesafe extends App {
  override def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("typesafe")
    val setActor = SetActorAdapter(actorSystem.actorOf(_, _))

    import actorSystem.dispatcher

    implicit val timeout = Timeout(1.second)

    for {
      _ <- setActor.add("foo")
      values1 <- setActor.all
      _ <- setActor.add("bar")
      values2 <- setActor.all
      _ <- setActor.remove("foo")
      values3 <- setActor.all
    } println(s"$values1\n$values2\n$values3")

    actorSystem.terminate
  }
}
