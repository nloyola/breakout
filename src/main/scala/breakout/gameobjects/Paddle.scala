package breakout.gameobjects

import breakout.Transform
import breakout.components.{ Component, RigidBody, Sprite, SpriteRenderer }
import org.joml.{ Vector2f, Vector3f }
import play.api.libs.json._
import breakout.{ KeyListener }
import org.lwjgl.glfw.GLFW.{ GLFW_KEY_A, GLFW_KEY_D }

import scala.collection.mutable.ArrayBuffer
import breakout.util.AssetPool

case class Paddle(gameWidth: Float, gameHeight: Float, protected val _zIndex: Int)
    extends AbstractGameObject {

  protected val name = "Paddle"

  private lazy val texture = AssetPool.texture("assets/images/paddle.png")

  protected lazy val _transform =
    Transform(new Vector2f(0, 10), new Vector2f(texture.width / 4f, texture.height / 4f))

  val rigidBody = RigidBody(1, new Vector3f(0, 0, 0), 0f)

  protected lazy val _components =
    ArrayBuffer[Component](SpriteRenderer(sprite = Sprite(texture)), rigidBody)

  private val paddleAbsSpeed = 500f

  private val moveRangeX = new Vector2f(0, gameWidth - _transform.scale.x)

  def posOffset(x: Float, y: Float): Unit = {
    transform.posOffset(x, y)
    ()
  }

  override def update(dt: Float): Unit = {
    val velocity =
      if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
        paddleAbsSpeed
      } else if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
        -paddleAbsSpeed
      } else {
        0
      }

    rigidBody.velocity.x = velocity

    _components.foreach(_.update(dt))

    // keep the paddle within the left and right limits
    _transform.position.x = Math.max(Math.min(_transform.position.x, moveRangeX.y), moveRangeX.x)

    ()
  }

  components.foreach(addComponent)

}

object Paddle {

  implicit val paddleFormat: Format[Paddle] = Json.format[Paddle]

}
