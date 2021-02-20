package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg.DenseVector

case class Paddle(private val _width: Float, private val _height: Float, protected val _zIndex: Int)
    extends Entity {

  val name = "Paddle"

  private lazy val texture = AssetPool.texture("assets/images/paddle.png")

  override protected val _transform: Transform = Transform(DenseVector(0f, 0f), DenseVector(_width, _height))

  val rigidBody = RigidBody(this, 1, 0f)

  def velocityX = rigidBody.velocityX

  def velocityX_=(v: Float) = rigidBody.velocityX = v

  scale(width, height)
  addComponent(SpriteRenderer(entity = this, sprite = Sprite(texture)))
  addComponent(rigidBody)
}
