package com.lunatech.workshops.paco.akkabasics.supervision

import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration.DurationInt
import scala.util.control.NonFatal

class Supervisor extends Actor {
  import context.dispatcher

  val child = context.actorOf(Props[Child])

  override def preStart(): Unit = {
    println(Console.MAGENTA + "Starting Supervisor" + Console.RESET)
    context.system.scheduler.scheduleOnce(1.second, self, "go")
  }

  override def postStop(): Unit = {
    println(Console.MAGENTA + "Supervisor stopped" + Console.RESET)
  }


  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println(Console.MAGENTA + "Supervisor restarting" + Console.RESET)
  }


  override def postRestart(reason: Throwable): Unit = {
    println(Console.MAGENTA + "Supervisor restarted" + Console.RESET)
  }


  override def receive: Receive = {
    case "go" =>
      child ! "die!"
      context.system.scheduler.scheduleOnce(1.second, self, "go")
  }
}

class Child extends Actor {
  override def preStart(): Unit = {
    println(Console.MAGENTA + "Starting Child" + Console.RESET)
  }


  override def postStop(): Unit = {
    println(Console.MAGENTA + "Child stopped" + Console.RESET)
  }


  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println(Console.MAGENTA + "Child restarting" + Console.RESET)
  }


  override def postRestart(reason: Throwable): Unit = {
    println(Console.MAGENTA + "Child restarted" + Console.RESET)
  }

  override def receive: Receive = {
    case "die!" =>
      println(Console.RED + "I’m dying :(" + Console.RESET)
      throw new Exception("boo!")
  }
}
