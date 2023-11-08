package microservice

import cats.effect._
import sttp.client4.quick._
import sttp.client4.Response
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.generic.semiauto._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import bot.config.Config

object WeatherMicroservice {
  def getWeather(city: String, weatherApi: String): IO[String] = {
    IO.blocking(
      quickRequest
        .get(uri"https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$weatherApi&units=metric")
        .send()
        .body
    )
  }
}
