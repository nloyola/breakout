package breakout.entities

import breakout.components.Component
import play.api.libs.json._

import scala.collection.mutable.ArrayBuffer
import breakout.Transform

case class GameObject(name: String, _transform: Transform, _zIndex: Int, _components: ArrayBuffer[Component])
    extends Entity {

  def posOffset(x: Float, y: Float): Unit = {
    _transform.posOffset(x, y)
    ()
  }

}

object GameObject {

  def apply(name: String): GameObject = GameObject(name, Transform(), 0, ArrayBuffer.empty[Component])

  def apply(name: String, transform: Transform, zIndex: Int): GameObject =
    GameObject(name, transform, zIndex, ArrayBuffer.empty[Component])

  implicit val gameObjectFormat: Format[GameObject] = Json.format[GameObject]

}
