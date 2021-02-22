package breakout.entities

import breakout.Transform
import breakout.components.Component
import breeze.linalg._

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

trait Entity {

  val name: String

  val transform: Transform

  val zIndex: Int

  protected val components = ArrayBuffer.empty[Component]

  def component[T]()(implicit tag: ClassTag[T]): Option[T] = {
    components.find {
      case e: T => true
      case _ => false
    } map { _.asInstanceOf[T] }
  }

  def addComponent(c: Component): Unit = {
    components += c
    ()
  }

  def removeComponent[T]()(implicit tag: ClassTag[T]): Unit = {
    components.filter {
      case e: T => true
      case _ => false
    }
    ()
  }

  def start(): Unit = components.foreach(_.start())

  def update(dt: Float): Unit = components.foreach(_.update(dt))

  def width = transform.scale(0)

  def height = transform.scale(1)

  def position = transform.position

  def position_=(pos: DenseVector[Float]) = transform.position := pos

  def posOffset(x: Float, y: Float): DenseVector[Float] = transform.position += DenseVector(x, y)

  def scale = transform.scale

  def scale_=(t: (Float, Float)) = {
    t match {
      case (x, y) => transform.scale := DenseVector(x, y)
    }
  }

  override def equals(that: Any): Boolean = {
    that match {
      case e: Entity => (name == e.name) && (transform == e.transform)
      case _ => false
    }
  }

  override def hashCode: Int = 41 * name.hashCode + transform.position.hashCode

  override def toString: String = s"name: $name, components: ${components.length}, zIndex: $zIndex"

}
