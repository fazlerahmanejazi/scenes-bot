package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter

import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(player: AudioPlayer) extends AudioEventAdapter {
  val queue = new LinkedBlockingQueue[BotTrack]()
  var nowPlaying: Option[BotTrack] = None


  def queue(track: BotTrack): Unit = {
    try {
      println("attempting to start a track")
      if (player.startTrack(track.fetchTrack().orNull, true)) {
        nowPlaying = Some(track)
      } else {
        println("putting track into the queue")
        queue.offer(track)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def nextTrack(): Boolean = {
    val empty = queue.isEmpty
    val nextTrack = Option(queue.poll())
    nowPlaying = nextTrack
    player.startTrack(nextTrack.flatMap(_.fetchTrack()).orNull, false)

    empty
  }

}
