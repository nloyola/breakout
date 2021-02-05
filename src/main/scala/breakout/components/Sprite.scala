package breakout.components

import breakout._
import breakout.renderers.Texture
import org.joml.Vector2f
import play.api.libs.json._

final case class Sprite(width: Int, height: Int, texture: Option[Texture], texCoords: Array[Vector2f]) {

  def texId(): Option[Int] = texture.map(_.id)

  override def toString: String = s"texture: $texture, texCoord: $texCoords"

}

object Sprite {

  def apply(): Sprite =
    Sprite(width = 0,
           height = 0,
           texture = None,
           texCoords =
             Array[Vector2f](new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1))
    )

  def apply(texture: Texture): Sprite =
    Sprite(width = 0,
           height = 0,
           texture = Some(texture),
           texCoords =
             Array[Vector2f](new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1))
    )

  implicit val spriteFormat: Format[Sprite] = Json.format[Sprite]

}
