package music


import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}

case class TrackInfo(title: String, artist: String)

case class BotTrack(fetchTrack: () => Option[AudioTrack], info: TrackInfo) {
  override def toString: String = {
    info.title + " - " + info.artist
  }
}