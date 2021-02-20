package breakout.components

import breakout.entities.Entity
import breeze.linalg._

case class RigidBody(entity: Entity, colliderType: Int, friction: Float) extends Component {

  protected val _velocity = DenseVector(0f, 0f, 0f)

  protected val typeName = "rigidBody"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    entity.position += _velocity.slice(0, 2) * dt
    ()
  }

  def velocity = _velocity

  def velocity_=(v: DenseVector[Float]) = _velocity := v

  def velocityX = _velocity(0)

  def velocityX_=(v: Float) = {
    _velocity(0) = v
  }

  def velocityY = _velocity(1)

  def velocityY_=(v: Float) = {
    _velocity(1) = v
  }

}
