package bot.integration

import cats.effect._
import cats.implicits._
import cats.Parallel

import org.typelevel.log4cats.slf4j.Slf4jLogger

import telegramium.bots._
import telegramium.bots.high.implicits.methodOps
import telegramium.bots.high.{Api, LongPollBot, Methods}

import microservice.WeatherMicroservice

class TelegramGate[F[_]](botApi: Api[F], weatherApi: String)(implicit
  asyncF: Async[F],
  parallel: Parallel[F],
  lifted: LiftIO[F]
) extends LongPollBot[F](botApi) {

  override def onMessage(message: Message): F[Unit] =
    message.text match {
      case Some("/start") =>
        sendGreetingMessage(message)

      case Some("/weather") =>
        handleWeatherCommand(message)

      case Some(text) if text.startsWith("/weather") =>
        val city = text.stripPrefix("/weather").trim
        handleWeatherInfoRequest(message, city)

      case _ =>
        handleUnknownCommand(message)
    }
  private def sendGreetingMessage(message: Message): F[Unit] =
    Methods
      .sendMessage(
        chatId = ChatIntId(message.chat.id),
        text =
          "Welcome to the Weather Bot! To get weather information, use the /weather command followed by a city name."
      )
      .exec(botApi)
      .void

  private def handleWeatherCommand(message: Message): F[Unit] =
    Methods
      .sendMessage(
        chatId = ChatIntId(message.chat.id),
        text = "Please specify a city name after /weather. For example: /weather Moscow"
      )
      .exec(botApi)
      .void

  private def handleWeatherInfoRequest(message: Message, city: String): F[Unit] =
    LiftIO[F]
      .liftIO(
        WeatherMicroservice
          .getWeather(city, weatherApi)
      )
      .flatMap(weatherInfo =>
        Methods
          .sendMessage(
            chatId = ChatIntId(message.chat.id),
            text = s"Weather information for $city: $weatherInfo"
          )
          .exec(botApi)
          .void
      )

  private def handleUnknownCommand(message: Message): F[Unit] =
    Methods
      .sendMessage(
        chatId = ChatIntId(message.chat.id),
        text = "I don't understand this command. Please use /weather followed by a city name."
      )
      .exec(botApi)
      .void

}
