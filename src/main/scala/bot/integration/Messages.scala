package bot.integration

import microservice.WeatherMicroservice

trait MessagesService {
  def weatherInfoToText(weatherInfo: microservice.WeatherResponse): String
  def greetingMessage: String
  def weatherCommandMessage: String
  def unknownCommandMessage: String
  def errorFetchingWeatherMessage(city: String): String
}

object Messages extends MessagesService {
  def weatherInfoToText(weatherInfo: microservice.WeatherResponse): String = {
    val iconMap = Map(
      "01d" -> "☀️",
      "02d" -> "🌤️",
      "03d" -> "☁️",
      "04d" -> "☁️",
      "09d" -> "🌧️",
      "10d" -> "🌦️",
      "11d" -> "⛈️",
      "13d" -> "❄️",
      "50d" -> "🌫️",
      "01n" -> "🌙",
      "02n" -> "🌤️",
      "03n" -> "☁️",
      "04n" -> "☁️",
      "09n" -> "🌧️",
      "10n" -> "🌦️",
      "11n" -> "⛈️",
      "13n" -> "❄️",
      "50n" -> "🌫️"
    )
    s"Weather information for ${weatherInfo.name}:\n" +
      s"\nTemperature: ${weatherInfo.main.temp} C°, feels like: ${weatherInfo.main.feels_like} C°\n" +
      s"${weatherInfo.weather.head.main}: ${weatherInfo.weather.head.description} ${iconMap(weatherInfo.weather.head.icon)}\n" +
      s"Wind speed: ${weatherInfo.wind.speed} m/s\n" +
      s"Sunrise: ${new java.util.Date(weatherInfo.sys.sunrise * 1000)} \n" +
      s"Sunset: ${new java.util.Date(weatherInfo.sys.sunset * 1000)}"
  }
  def greetingMessage: String = {
    s"Welcome to the Weather Bot! To get weather information, use the /weather command followed by a city name"
  }

  def weatherCommandMessage: String = {
    s"Please specify a city name after /weather. For example: /weather Moscow"
  }

  def unknownCommandMessage: String = {
    s"I don't understand this command. Please use /weather followed by a city name"
  }

  def errorFetchingWeatherMessage(city: String): String = {
    s"Error fetching weather information for $city.\nPlease try again with a valid city name"
  }
}
