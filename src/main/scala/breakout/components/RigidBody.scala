package breakout.components

import breakout._
import org.joml.Vector3f
import play.api.libs.json._

case class RigidBody(colliderType: Int, velocity: Vector3f, friction: Float) extends Component {

  protected val typeName = "rigidBody"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    _entity.map(_.posOffset(velocity.x * dt, velocity.y * dt))
    ()
  }

}

object RigidBody {

  implicit val rigidBodyFormat: Format[RigidBody] = Json.format[RigidBody]

}
