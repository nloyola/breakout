package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg._

case class Ball(radius: Float, zIndex: Int) extends Entity {

  override val name: String = "Ball"

  override val transform: Transform =
    Transform(DenseVector(0f, 0f), DenseVector(2f * radius, 2 * radius))

  private val sprite = Sprite(AssetPool.texture("assets/images/ball.png"))

  private val spriteRenderer = SpriteRenderer(this, sprite)

  private val rigidBody = RigidBody(this, 1, 0f)

  private var _stuck = true

  def velocity = rigidBody.velocity

  def velocity_=(v: DenseVector[Float]) = rigidBody.velocity := v

  def velocityX = rigidBody.velocityX

  def velocityX_=(v: Float) = rigidBody.velocityX = v

  def velocityY = rigidBody.velocityY

  def velocityY_=(v: Float) = rigidBody.velocityY = v

  def stuck = _stuck

  def stuck_=(v: Boolean) = _stuck = v

  addComponent(spriteRenderer)
  addComponent(rigidBody)
}
