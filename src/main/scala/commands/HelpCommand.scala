package commands

import handlers.{CommandHandler, MusicHandler}
import net.dv8tion.jda.api.entities.{Guild, MessageChannel}
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class HelpCommand() extends CommandHandler {
  override val command: Seq[String] = List("help")
  override val helpInfo: String = "Help Info"

  override protected def execute(executorName: String, args: String): String = ""

  override def executeCustom(executerName: String, args: String, event: MessageReceivedEvent, channel: MessageChannel): String = {
    val guild: Guild = event.getGuild

    val helpText = "`\n" +
      "~play <url> \n" +
      "~skip \n" +
      "~queue \n" +
      "~help \n" +
      "~rd <number>" +
      "`"

    println(args)

    channel.sendMessage(helpText).queue()
    ""
  }
}
