package music

import com.sedmelluq.discord.lavaplayer.player.{AudioPlayer, AudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import handlers.AudioPlayerSendHandler

class GuildMusicManager(manager: AudioPlayerManager) {
  val player: AudioPlayer = manager.createPlayer()
  val volume = 20
  player.setVolume(volume)

  val scheduler = new TrackScheduler(player)
  player.addListener(scheduler)

  def getSendHandler: AudioPlayerSendHandler = {
    new AudioPlayerSendHandler((player))
  }

}
