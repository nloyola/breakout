package breakout.components

import breakout._
import breakout.entities.Entity
import org.joml.{ Vector2f, Vector3f }
import play.api.libs.json._
//import breakout.entities.BallParticle

case class RigidBody(entity: Entity, colliderType: Int, _velocity: Vector3f, friction: Float)
    extends Component {

  protected val typeName = "rigidBody"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    // entity match {
    //   case p: BallParticle =>
    //     if ((_velocity.x != 0f) || (_velocity.x != 0f) || (_velocity.x != 0f)) {
    //       org.slf4j.LoggerFactory.getLogger(this.getClass).info(s"---------->  ${_velocity}")
    //     }
    //   case _ =>
    // }

    entity.posOffset(_velocity.x * dt, _velocity.y * dt)
    ()
  }

  def velocity = _velocity

  def velocity_=(v: Vector2f) = {
    _velocity.x = v.x
    _velocity.y = v.y
  }

  def velocityX = _velocity.x

  def velocityX_=(v: Float) = {
    _velocity.x = v
  }

  def velocityY = _velocity.y

  def velocityY_=(v: Float) = {
    _velocity.y = v
  }

}

object RigidBody {

  implicit val rigidBodyFormat: Format[RigidBody] = Json.format[RigidBody]

}
