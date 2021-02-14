package breakout.entities

import breakout.Transform
import breakout.components.Component
import org.joml.Vector2f
import play.api.libs.json._

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

trait Entity {

  val name: String

  protected val _transform: Transform

  protected val _zIndex: Int

  protected val _components = ArrayBuffer.empty[Component]

  def transform = _transform

  def zIndex = _zIndex

  def components = _components

  def component[T]()(implicit tag: ClassTag[T]): Option[T] = {
    _components.find {
      case e: T => true
      case _ => false
    } map { _.asInstanceOf[T] }
  }

  def addComponent(c: Component): Unit = {
    _components += c
    ()
  }

  def removeComponent[T]()(implicit tag: ClassTag[T]): Unit = {
    _components.filter {
      case e: T => true
      case _ => false
    }
    ()
  }

  def start(): Unit = _components.foreach(_.start())

  def update(dt: Float): Unit = _components.foreach(_.update(dt))

  def width = _transform.scale.x

  def height = _transform.scale.y

  def position = _transform.position

  def position_=(pos: Vector2f) = _transform.position.set(pos)

  def scale = _transform.scale

  def posOffset(x: Float, y: Float): Unit = {
    _transform.posOffset(x, y)
    ()
  }

  def scale(x: Float, y: Float): Unit = {
    _transform.setScale(x, y)
    ()
  }

  override def equals(that: Any): Boolean = {
    that match {
      case e: Entity => (name == e.name) && (_transform == e._transform)
      case _ => false
    }
  }

  override def hashCode: Int = 41 * name.hashCode + _transform.position.hashCode

  override def toString: String = s"name: $name, components: ${_components.length}, zIndex: $zIndex"

}

object Entity {

  implicit val abstractGameObjectFormat: Format[Entity] = new Format[Entity] {

    override def writes(c: Entity): JsValue = {
      val objJson = c match {
        case i: GameObject => Json.toJson(i)
        case i: Paddle     => Json.toJson(i)
        case _ => JsNull
      }

      Json.obj("name" -> c.name) ++ objJson.as[JsObject]
    }

    override def reads(json: JsValue): JsResult[Entity] =
      (json \ "name") match {
        case JsDefined(JsString("spriteRenderer")) => json.validate[GameObject]
        case JsDefined(JsString("rigidBody"))      => json.validate[Paddle]
        case _                                     => JsError("error")
      }
  }
}
