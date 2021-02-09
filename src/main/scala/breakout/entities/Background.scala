package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import org.joml.Vector2f
import play.api.libs.json._

import breakout.util.AssetPool

case class Background(private val _width: Float, private val _height: Float, protected val _zIndex: Int)
    extends Entity {

  val name = "Background"

  private lazy val tex = AssetPool.texture("assets/images/background.png")

  protected lazy val _transform = Transform(new Vector2f(), new Vector2f(_width, _height))

  override def posOffset(x: Float, y: Float): Unit = {
    throw new Error("background should not move")
  }

  addComponent(SpriteRenderer(sprite = Sprite(tex)))

}

object Background {

  implicit val backgroundFormat: Format[Background] = Json.format[Background]

}
