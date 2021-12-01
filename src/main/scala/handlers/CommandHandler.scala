package handlers

import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

abstract class CommandHandler {
  val command: Seq[String]
  val helpInfo: String
  val usageText: String = ""

  def usage: Option[String] = if (usageText.isEmpty) None else Some(s"${command.head} $usageText")

//  def onMessage(event: MessageReceivedEvent, name: String, message: String, channel: MessageChannel): Unit

  protected def execute(executorName: String, args: String): String

  def executeCustom(executorName: String, args: String, event: MessageReceivedEvent, channel: MessageChannel): String = execute(executorName, args)
}
