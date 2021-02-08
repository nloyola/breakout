package breakout

import org.joml.Vector2f
import play.api.libs.json._

case class Transform(position: Vector2f, scale: Vector2f) {

  def copy(): Transform = Transform(new Vector2f(position), new Vector2f(scale))

  def posOffset(x: Float, y: Float) = position.set(position.x + x, position.y + y)

  def setScale(x: Float, y: Float) = scale.set(x, y)

  override def equals(that: Any): Boolean = {
    that match {
      case t: Transform => position.equals(t.position) && scale.equals(t.scale)
      case _ => false
    }
  }

  override def hashCode: Int = 41 * position.hashCode + scale.hashCode

  override def toString: String = s"position: $position, scale: ${scale}"
}

object Transform {

  def apply(): Transform = Transform(new Vector2f(), new Vector2f())

  def apply(position: Vector2f): Transform = Transform(position, new Vector2f())

  implicit val transformFormat: Format[Transform] = Json.format[Transform]

}
