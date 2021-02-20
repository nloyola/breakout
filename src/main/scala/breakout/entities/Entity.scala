package breakout.entities

import breakout.Transform
import breakout.components.Component
import breeze.linalg._

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

  def width = _transform.scale(0)

  def height = _transform.scale(1)

  def position = _transform.position

  def position_=(pos: DenseVector[Float]) = _transform.position := pos

  def posOffset(x: Float, y: Float): DenseVector[Float] = {
    _transform.position += DenseVector(x, y)
  }

  def scale = _transform.scale

  def scale(x: Float, y: Float) = _transform.scale := DenseVector(x, y)

  override def equals(that: Any): Boolean = {
    that match {
      case e: Entity => (name == e.name) && (_transform == e._transform)
      case _ => false
    }
  }

  override def hashCode: Int = 41 * name.hashCode + _transform.position.hashCode

  override def toString: String = s"name: $name, components: ${_components.length}, zIndex: $zIndex"

}
