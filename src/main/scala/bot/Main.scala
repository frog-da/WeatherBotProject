package bot

import cats.effect._
import cats.syntax.all._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import telegramium.bots.high.{Api, BotApi, LongPollBot}
import bot.config.Config
import bot.integration.TelegramGate
import microservice.WeatherMicroservice
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.client.middleware.Logger

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import cats.effect.ExitCode
import cats.effect.IOApp

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    Config.load.fold(
      error => log.error(s"Could not load config file: $error").as(ExitCode.Error),
      config =>
        log.info(s"loaded config: $config") >>
          resources(config)
            .use(httpClient => assembleAndLaunch(config, httpClient))
            .as(ExitCode.Success)
    )
  }

  private def assembleAndLaunch(config: Config, httpClient: Client[IO]): IO[Unit] = {
    val client = Logger(logHeaders = false, logBody = false)(httpClient)
    val botApi = BotApi[IO](client, s"https://api.telegram.org/bot${config.tgBotApiToken}")
    val weatherApi = config.weatherServiceApiToken
    val weatherMicroservice = WeatherMicroservice[IO]
    new TelegramGate[IO](botApi, weatherApi, weatherMicroservice).start().void

  }

  private def resources(config: Config): Resource[IO, Client[IO]] =
    BlazeClientBuilder[IO]
      .withResponseHeaderTimeout(FiniteDuration(telegramResponseWaitTime, TimeUnit.SECONDS))
      .resource

  private val telegramResponseWaitTime = 60L
  private val log = Slf4jLogger.getLoggerFromName[IO]("application")
}
