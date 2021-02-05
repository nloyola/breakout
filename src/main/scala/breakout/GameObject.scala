package breakout

import breakout.components.Component
import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import play.api.libs.json._

case class GameObject(name: String, transform: Transform, zIndex: Int, components: ArrayBuffer[Component]) {

  def component[T]()(implicit tag: ClassTag[T]): Option[T] = {
    components.find {
      case e: T => true
      case _ => false
    } map { _.asInstanceOf[T] }
  }

  def removeComponent[T]()(implicit tag: ClassTag[T]): Unit = {
    components.filter {
      case e: T => true
      case _ => false
    }
    ()
  }

  def addComponent(c: Component): Unit = {
    components += c
    c.gameObject = Some(this)
  }

  def update(dt: Float): Unit = components.foreach(_.update(dt))

  def start(): Unit = components.foreach(_.start())

  def getZIndex(): Int = zIndex

  override def toString: String = s"name: $name, components: ${components.length}, zIndex: $zIndex"

}

object GameObject {

  def apply(name: String): GameObject = GameObject(name, Transform(), 0, ArrayBuffer.empty[Component])

  def apply(name: String, transform: Transform, zIndex: Int): GameObject =
    GameObject(name, transform, zIndex, ArrayBuffer.empty[Component])

  implicit val gameObjectFormat: Format[GameObject] = Json.format[GameObject]

}
