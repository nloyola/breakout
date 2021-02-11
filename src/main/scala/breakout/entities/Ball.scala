package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import org.joml.{ Vector2f, Vector3f }

import breakout.util.AssetPool

case class Ball(private val _radius: Float, protected val _zIndex: Int) extends Entity {

  override val name: String = "Ball"

  private val diameter = 2f * _radius

  override protected val _transform: Transform = Transform(new Vector2f(), new Vector2f(diameter, diameter))

  private val sprite = Sprite(AssetPool.texture("assets/images/ball.png"))

  private val spriteRenderer = SpriteRenderer(sprite = sprite)

  private val rigidBody = RigidBody(1, new Vector3f(), 0f)

  private var _stuck = true

  def velocity = rigidBody.velocity

  def velocity_=(v: Vector2f) = {
    rigidBody.velocity.x = v.x
    rigidBody.velocity.y = v.y
  }

  def stuck = _stuck

  def stuck_=(v: Boolean) = _stuck = v

  def radius = _transform.scale.x()

  def radius_=(r: Float) = _transform.setScale(r, r)

  addComponent(spriteRenderer)
  addComponent(rigidBody)
}
