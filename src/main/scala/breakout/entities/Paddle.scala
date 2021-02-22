package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool

case class Paddle(zIndex: Int) extends Entity {

  val name = "Paddle"

  private lazy val texture = AssetPool.texture("assets/images/paddle.png")

  override val transform = Transform()

  val rigidBody = RigidBody(this, 1, 0f)

  def velocityX = rigidBody.velocityX

  def velocityX_=(v: Float) = rigidBody.velocityX = v

  scale = (width, height)
  addComponent(SpriteRenderer(entity = this, sprite = Sprite(texture)))
  addComponent(rigidBody)
}
