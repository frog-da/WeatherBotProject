package bot.config

import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._

case class Config(tgBotApiToken: String, weatherServiceApiToken: String)

object Config {
  def load: Result[Config] =
    ConfigSource.default.load[Config]
}
