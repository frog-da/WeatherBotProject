package bot.integration

import cats.effect._
import cats.implicits._
import cats.Parallel
import org.typelevel.log4cats.slf4j.Slf4jLogger
import telegramium.bots._
import telegramium.bots.high.implicits.methodOps
import telegramium.bots.high.{Api, LongPollBot, Methods}
import microservice.WeatherMicroservice
import Messages._

class TelegramGate[F[_]: Async: Parallel](
  botApi: Api[F],
  weatherApi: String,
  weatherMicroservice: WeatherMicroservice[F]
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
        text = Messages.greetingMessage
      )
      .exec(botApi)
      .void

  private def handleWeatherCommand(message: Message): F[Unit] =
    Methods
      .sendMessage(
        chatId = ChatIntId(message.chat.id),
        text = Messages.weatherCommandMessage
      )
      .exec(botApi)
      .void

  private def handleWeatherInfoRequest(message: Message, city: String): F[Unit] =
    weatherMicroservice.getWeather(city, weatherApi).flatMap {
      case Right(weatherInfo) =>
        Methods
          .sendMessage(
            chatId = ChatIntId(message.chat.id),
            text = Messages.weatherInfoToText(weatherInfo)
          )
          .exec(botApi)
          .void
      case Left(_) =>
        Methods
          .sendMessage(
            chatId = ChatIntId(message.chat.id),
            text = Messages.errorFetchingWeatherMessage(city)
          )
          .exec(botApi)
          .void
    }

  private def handleUnknownCommand(message: Message): F[Unit] =
    Methods
      .sendMessage(
        chatId = ChatIntId(message.chat.id),
        text = Messages.unknownCommandMessage
      )
      .exec(botApi)
      .void
}
