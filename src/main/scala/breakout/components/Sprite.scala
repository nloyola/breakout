package breakout.components

import org.joml.Vector2f
import breakout._
import breakout.renderers.Texture
import play.api.libs.json._

final case class Sprite(width: Int, height: Int, texture: Option[Texture], texCoords: List[Vector2f]) {

  def texId(): Option[Int] = texture.map(_.texId())

  override def toString: String = s"texture: $texture, texCoord: $texCoords"

}

object Sprite {

  def apply(texture: Option[Texture]): Sprite =
    Sprite(0,
           0,
           texture,
           List(new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1))
    )

  implicit val spriteFormat: Format[Sprite] = Json.format[Sprite]

}
