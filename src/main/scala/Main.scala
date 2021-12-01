package Main

import Bot.Bot
import com.typesafe.config._
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent.{GUILD_MESSAGES, GUILD_VOICE_STATES}

object Main extends App {

  val conf = ConfigFactory.load()
  val bot = new Bot()

  val jda = JDABuilder
    .create(conf.getString("scenes-bot.token"), GUILD_MESSAGES, GUILD_VOICE_STATES)
    .addEventListeners(bot)
    .build()
}