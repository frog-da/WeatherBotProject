package bot

import cats.effect._
import cats.syntax.all._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import telegramium.bots.high.{Api, BotApi, LongPollBot}
import bot.config.Config
import bot.integration.TelegramGate
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.client.middleware.Logger

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.effect.ExitCode
import cats.effect.IOApp

object Main extends IOApp {
  type F[+T] = IO[T]

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
  
  private def assembleAndLaunch(config: Config, httpClient: Client[F]): IO[Unit] = {
    val client = Logger(logHeaders = false, logBody = false)(httpClient)
    val api    = BotApi[F](client, s"https://api.telegram.org/bot${config.tgBotApiToken}")
    for {
      _ <- new TelegramGate[F](api).start().void
    } yield ()
  }


  private def resources(config: Config): Resource[F, Client[F]] =
    BlazeClientBuilder[F]
      .withResponseHeaderTimeout(FiniteDuration(telegramResponseWaitTime, TimeUnit.SECONDS))
      .resource

  private val telegramResponseWaitTime = 60L
  private val log = Slf4jLogger.getLoggerFromName[F]("application")
}