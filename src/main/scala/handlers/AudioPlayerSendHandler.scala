package handlers

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler

import java.nio.ByteBuffer

class AudioPlayerSendHandler(audioPlayer: AudioPlayer) extends AudioSendHandler {
  private var lastFrame: Option[AudioFrame] = None

  override def provide20MsAudio(): ByteBuffer = {
    if (lastFrame.isEmpty) {
      lastFrame = Option(audioPlayer.provide())
    }

    val data: ByteBuffer = lastFrame match {
      case Some(frame) => ByteBuffer.wrap(frame.getData)
      case _ => null
    }

    data
  }

  override def canProvide: Boolean = {
    lastFrame = Option(audioPlayer.provide())

    lastFrame.isDefined
  }

  override def isOpus: Boolean = {
    true
  }
}
