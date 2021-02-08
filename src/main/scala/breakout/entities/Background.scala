package breakout.entities

import breakout.Transform
import breakout.components.{ Component, Sprite, SpriteRenderer }
import org.joml.Vector2f
import play.api.libs.json._

import scala.collection.mutable.ArrayBuffer
import breakout.util.AssetPool

case class Background(gameWidth: Float, gameHeight: Float, protected val _zIndex: Int)
    extends Entity {

  protected val name = "Background"

  private lazy val tex = AssetPool.texture("assets/images/background.png")

  protected lazy val _transform = Transform(new Vector2f(0, 0), new Vector2f(gameWidth, gameHeight))

  protected lazy val _components = ArrayBuffer[Component](SpriteRenderer(sprite = Sprite(tex)))

  override def update(dt: Float): Unit = {
    _components.foreach(_.update(dt))
    ()
  }

  def posOffset(x: Float, y: Float): Unit = {
    throw new Error("background should not move")
  }

  components.foreach(addComponent)

}

object Background {

  implicit val backgroundFormat: Format[Background] = Json.format[Background]

}
