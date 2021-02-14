package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool
import org.joml.{ Vector2f, Vector3f }

case class Ball(private val _radius: Float, protected val _zIndex: Int) extends Entity {

  override val name: String = "Ball"

  private val diameter = 2f * _radius

  override protected val _transform: Transform = Transform(new Vector2f(), new Vector2f(diameter, diameter))

  private val sprite = Sprite(AssetPool.texture("assets/images/ball.png"))

  private val spriteRenderer = SpriteRenderer(this, sprite)

  private val rigidBody = RigidBody(this, 1, new Vector3f(), 0f)

  private var _stuck = true

  def velocity = rigidBody._velocity

  def velocity_=(v: Vector2f) = rigidBody.velocity = v

  def velocityX = rigidBody._velocity.x

  def velocityX_=(v: Float) = rigidBody._velocity.x = v

  def velocityY = rigidBody._velocity.y

  def velocityY_=(v: Float) = rigidBody._velocity.y = v

  def stuck = _stuck

  def stuck_=(v: Boolean) = _stuck = v

  def radius = _radius

  def radius_=(r: Float) = _transform.setScale(r / 2f, r / 2f)

  addComponent(spriteRenderer)
  addComponent(rigidBody)
}
