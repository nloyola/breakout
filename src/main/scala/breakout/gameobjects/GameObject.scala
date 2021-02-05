package breakout.gameobjects

import breakout.components.Component
import play.api.libs.json._

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import breakout.Transform

trait AbstractGameObject {

  protected val name: String

  protected val _transform: Transform

  protected val _zIndex: Int

  protected val _components: ArrayBuffer[Component]

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
    c.gameObject = this
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

  def posOffset(x: Float, y: Float): Unit

  override def toString: String = s"name: $name, components: ${_components.length}, zIndex: $zIndex"

}

case class GameObject(name: String, _transform: Transform, _zIndex: Int, _components: ArrayBuffer[Component])
    extends AbstractGameObject {

  def posOffset(x: Float, y: Float): Unit = {
    _transform.posOffset(x, y)
    ()
  }

}

object AbstractGameObject {

  implicit val abstractGameObjectFormat: Format[AbstractGameObject] = new Format[AbstractGameObject] {

    override def writes(c: AbstractGameObject): JsValue = {
      val objJson = c match {
        case i: GameObject => Json.toJson(i)
        case i: Paddle     => Json.toJson(i)
        case _ => JsNull
      }

      Json.obj("name" -> c.name) ++ objJson.as[JsObject]
    }

    override def reads(json: JsValue): JsResult[AbstractGameObject] =
      (json \ "name") match {
        case JsDefined(JsString("spriteRenderer")) => json.validate[GameObject]
        case JsDefined(JsString("rigidBody"))      => json.validate[Paddle]
        case _                                     => JsError("error")
      }
  }
}

object GameObject {

  def apply(name: String): GameObject = GameObject(name, Transform(), 0, ArrayBuffer.empty[Component])

  def apply(name: String, transform: Transform, zIndex: Int): GameObject =
    GameObject(name, transform, zIndex, ArrayBuffer.empty[Component])

  implicit val gameObjectFormat: Format[GameObject] = Json.format[GameObject]

}
