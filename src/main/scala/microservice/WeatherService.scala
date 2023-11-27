package microservice

import cats.effect._
import sttp.client4.quick._
import upickle.default._
import cats.syntax.all._
import cats.ApplicativeThrow


case class WeatherResponse(
  coord: Coord,
  weather: List[Weather],
  main: Main,
  wind: Wind,
  visibility: Int,
  clouds: Clouds,
  sys: Sys,
  name: String
)

object WeatherResponse {
  implicit val reader: upickle.default.Reader[WeatherResponse] = upickle.default.macroR
}

case class Coord(lon: Float, lat: Float)
case class Weather(id: Int, main: String, description: String, icon: String)
case class Main(temp: Double, feels_like: Double, temp_min: Double, temp_max: Double, pressure: Int, humidity: Int)
case class Wind(speed: Double, deg: Int)
case class Clouds(all: Int)
case class Sys(`type`: Int, id: Int, country: String, sunrise: Long, sunset: Long)

object Coord {
  implicit val reader: upickle.default.Reader[Coord] = upickle.default.macroR
}
object Weather {
  implicit val reader: upickle.default.Reader[Weather] = upickle.default.macroR
}
object Main {
  implicit val reader: upickle.default.Reader[Main] = upickle.default.macroR
}
object Wind {
  implicit val reader: upickle.default.Reader[Wind] = upickle.default.macroR
}
object Clouds {
  implicit val reader: upickle.default.Reader[Clouds] = upickle.default.macroR
}
object Sys {
  implicit val reader: upickle.default.Reader[Sys] = upickle.default.macroR
}

trait WeatherMicroservice[F[_]] {
  def getWeather(city: String, weatherApi: String): F[Either[String, WeatherResponse]]
}

object WeatherMicroservice {
  def apply[F[_]: Sync: ApplicativeThrow]: WeatherMicroservice[F] = new WeatherMicroservice[F] {
    override def getWeather(city: String, weatherApi: String): F[Either[String, WeatherResponse]] =
      Sync[F].blocking {
        implicit val reader: upickle.default.Reader[WeatherResponse] = upickle.default.macroR
        val response = quickRequest
          .get(
            uri"https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$weatherApi&units=metric&exclude=minutely,hourly,daily,alerts"
          )
          .send()

        if (response.isSuccess) {
          try {
            val json = response.body
            Right(read[WeatherResponse](json))
          } catch {
            case e: Exception => Left(s"Error parsing weather data for $city: ${e.getMessage}")
          }
        } else {
          Left(s"Failed to retrieve weather data for $city. Status code: ${response.code}")
        }
      }
  }
}
