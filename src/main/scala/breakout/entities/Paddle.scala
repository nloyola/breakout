package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import org.joml.{ Vector2f, Vector3f }
import play.api.libs.json._

import breakout.util.AssetPool

case class Paddle(private val _width: Float, private val _height: Float, protected val _zIndex: Int)
    extends Entity {

  val name = "Paddle"

  private lazy val texture = AssetPool.texture("assets/images/paddle.png")

  override protected val _transform: Transform = Transform(new Vector2f(), new Vector2f(_width, _height))

  val rigidBody = RigidBody(1, new Vector3f(), 0f)

  def velocityX = rigidBody.velocity.x

  def velocityX_=(v: Float) = rigidBody.velocity.x = v

  scale(width, height)
  addComponent(SpriteRenderer(sprite = Sprite(texture)))
  addComponent(rigidBody)
}

object Paddle {

  implicit val paddleFormat: Format[Paddle] = Json.format[Paddle]

}
