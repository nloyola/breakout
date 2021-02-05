package breakout.gameobjects

import breakout.Transform
import breakout.components.{ Component, RigidBody, Sprite, SpriteRenderer }
import breakout.renderers.Texture
import org.joml.{ Vector2f, Vector3f }
import play.api.libs.json._
import breakout.{ KeyListener }
import org.lwjgl.glfw.GLFW.{ GLFW_KEY_A, GLFW_KEY_D }

import scala.collection.mutable.ArrayBuffer

case class Paddle(protected val _zIndex: Int) extends AbstractGameObject {

  protected val name = "Paddle"

  private lazy val tex = Texture("assets/images/paddle.png")

  protected lazy val _transform =
    Transform(new Vector2f(0, 10), new Vector2f(tex.width / 4f, tex.height / 4f))

  val rigidBody = RigidBody(1, new Vector3f(0, 0, 0), 0.8f)

  protected lazy val _components =
    ArrayBuffer[Component](SpriteRenderer(sprite = Sprite(tex)), rigidBody)

  private val paddleAbsSpeed = 300f

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
    ()
  }

  components.foreach(addComponent)

}

object Paddle {

  implicit val paddleFormat: Format[Paddle] = Json.format[Paddle]

}
