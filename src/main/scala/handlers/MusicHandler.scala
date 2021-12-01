package handlers

import com.sedmelluq.discord.lavaplayer.player.{AudioLoadResultHandler, DefaultAudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}
import music.{BotTrack, GuildMusicManager, TrackInfo, TrackScheduler}
import net.dv8tion.jda.api.entities.{Guild, MessageChannel, VoiceChannel}
import net.dv8tion.jda.api.managers.AudioManager

import scala.collection.JavaConverters._
import scala.collection.mutable

class MusicHandler {

  private val playerManager = new DefaultAudioPlayerManager()
  AudioSourceManagers.registerRemoteSources(playerManager)
  AudioSourceManagers.registerLocalSource(playerManager)

  private val musicManagers = mutable.Map[String, GuildMusicManager]()
  private var lastActiveChannel: Option[MessageChannel] = None


  def getGuildAudioPlayer(guild: Guild): GuildMusicManager = {
    val id = guild.getId

    val musicManager = musicManagers.get(id) match {
      case Some(manager) =>
        manager
      case _ =>
        val manager = new GuildMusicManager(playerManager)
        musicManagers += (id -> manager)
        manager
    }

    guild.getAudioManager.setSendingHandler(musicManager.getSendHandler)

    musicManager
  }

  def onTrackStart(track: AudioTrack): Unit = {
    lastActiveChannel.foreach(_.sendMessage("Now playing: "+ track.getInfo.title))
  }

  def loadAndPlay(channel: MessageChannel, guild: Guild, toJoin: VoiceChannel, trackSearch: String): Unit = {
    val musicManager = getGuildAudioPlayer(guild)
    val content = trackSearch.split(" ").tail.mkString(" ")

    lastActiveChannel = Some(channel)
    println("url", content)

    playerManager.loadItemOrdered(musicManager, content, new AudioLoadResultHandler {
      override def trackLoaded(track: AudioTrack): Unit = {
        play(guild, musicManager, BotTrack(() => Some(track), TrackInfo(track.getInfo.title, track.getInfo.author)), toJoin)
        channel.sendMessage("added track to the queue: **" + track.getInfo.title + "**").queue()
      }

      override def playlistLoaded(playlist: AudioPlaylist): Unit = {
        val firstTrack = Option(playlist.getSelectedTrack).getOrElse(playlist.getTracks.get(0))
        for (track <- playlist.getTracks.asScala) {
          play(guild, musicManager, BotTrack(() => Some(track), TrackInfo(track.getInfo.title, track.getInfo.author)), toJoin)
        }

        channel.sendMessage("adding to queue **" + firstTrack.getInfo.title + "** (from playlist **" + playlist.getName + "**)").queue()
      }


      override def loadFailed(exception: FriendlyException): Unit = {
        channel.sendMessage("duh. which song is that?").queue()
        exception.printStackTrace()
      }

      override def noMatches(): Unit = {
        channel.sendMessage("duh. which song is that?").queue()
      }
    })
  }

  def play(guild: Guild, musicManager: GuildMusicManager, track: BotTrack, voiceChannel: VoiceChannel): Unit = {
    connectToVoiceChannel(voiceChannel, guild.getAudioManager)

    musicManager.scheduler.queue(track)
  }

  def skipTrack(guild: Guild): Boolean = {
    val wasSkipped = getGuildAudioPlayer(guild).scheduler.nowPlaying.isDefined
    getGuildAudioPlayer(guild).scheduler.nextTrack()
    wasSkipped
  }

  def join(guild: Guild, voiceChannel: VoiceChannel): Unit = {
    connectToVoiceChannel(voiceChannel, guild.getAudioManager)
  }

  def listQueue(guild: Guild, channel: MessageChannel): String = {
    val scheduler: TrackScheduler = getGuildAudioPlayer(guild).scheduler
    val tracks = scheduler.nowPlaying ++ scheduler.queue.asScala.toList
    println(tracks)

    if (tracks.isEmpty) {
      "there's nothing in the queue"
    } else {
      val trackList = "`\n" +
        tracks.zipWithIndex.map {
        case(track: BotTrack, i: Int) =>
          (i + 1) + "." + track.toString
      }.mkString("\n") + "\n`"

      channel.sendMessage(trackList).queue()
      "printed queue"
    }
  }

  def connectToVoiceChannel(voiceChannel: VoiceChannel, audioManager: AudioManager): Unit = {
    if (!audioManager.isConnected && !audioManager.isAttemptingToConnect) {
      audioManager.openAudioConnection(voiceChannel)
    }
  }
}
