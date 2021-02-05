package breakout.components

import breakout._
import play.api.libs.json._
import org.joml.Vector3f
import org.joml.Vector4f
import org.slf4j.LoggerFactory

case class RigidBody(var colliderType: Int, var velocity: Vector3f, var friction: Float, var tmp: Vector4f)
    extends Component {

  protected val log      = LoggerFactory.getLogger(this.getClass)
  protected val typeName = "rigidBody"

  var gameObject: Option[GameObject] = None

  override def start(): Unit = {}

}

object RigidBody {

  implicit val rigidBodyFormat: Format[RigidBody] = Json.format[RigidBody]

}
