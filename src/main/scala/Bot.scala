package Bot

import commands.{HelpCommand, MusicCommand, RollCommand}
import handlers.CommandHandler
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Bot extends ListenerAdapter {

  val commands: List[CommandHandler] = List(
    new MusicCommand(), new HelpCommand(), new RollCommand()
  )


  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    val name: String = event.getAuthor.getName
    val channel: MessageChannel = event.getChannel
    val message: String = event.getMessage.getContentDisplay

    println(s"....listening to: $message")

    if (message.startsWith("~") && !event.getAuthor.isBot) {
      val command: String = message.tail.split(" ").headOption.getOrElse("")
      val content: String = message.split(" ").tail.mkString(" ")
      val args: String = command + " " + content

      println(command, content)

//      musicCommand.executeCustom(name, args, event, channel)
      val result = commands
        .find(c => c.command.contains(command))
        .map(_.executeCustom(name, args, event, channel))
      }
  }

}

