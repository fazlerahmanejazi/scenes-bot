package commands

import handlers.{CommandHandler, MusicHandler}
import net.dv8tion.jda.api.entities.{Guild, MessageChannel}
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import scala.util.Random



class RollCommand() extends CommandHandler {
  override val command: Seq[String] = List("rd")
  override val helpInfo: String = "Roll dice"

  override protected def execute(executorName: String, args: String): String = ""

  override def executeCustom(executerName: String, args: String, event: MessageReceivedEvent, channel: MessageChannel): String = {
    val guild: Guild = event.getGuild
    args.split(" ").head.trim() match {
      case "rd" =>

        val maxValue = args.split(" ").last.trim.toInt
        val result = Random.between(1, maxValue + 1)
        val text = "`\n" +
          s"$executerName rolled $result" +
          "`"

        channel.sendMessage(text).queue()
        ""
    }
  }
}
