package commands

import handlers.{CommandHandler, MusicHandler}
import music.BotTrack
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.entities.{Guild, MessageChannel, VoiceChannel}

class MusicCommand() extends CommandHandler {
  override val command: Seq[String] = List("music", "play", "skip", "queue")
  override val helpInfo: String = "Play music"
  val musicHandler = new MusicHandler()

  override protected def execute(executorName: String, args: String): String = ""

  override def executeCustom(executerName: String, args: String, event: MessageReceivedEvent, channel: MessageChannel): String = {
    val guild: Guild = event.getGuild

    args.split(" ").head.trim() match {
      case "play" =>
        val voiceChannelPresence = Option(event.getMember.getVoiceState.getChannel)

        voiceChannelPresence match {
          case Some(voiceChannel) =>
            musicHandler.loadAndPlay(channel, guild, voiceChannel, args)
            println("loading and playing")
            "loading and playing"
          case _ =>
            "you're not in a voice channel."
        }

      case "skip" =>
        val wasSkipped = musicHandler.skipTrack(guild)
        ""

      case "queue" =>
        musicHandler.listQueue(guild, channel)
    }
  }
}
