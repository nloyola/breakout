package breakout

import breeze.linalg._

case class Transform(position: DenseVector[Float], scale: DenseVector[Float]) {

  def copy(): Transform = Transform(position.copy, scale.copy)

  def posOffset(x: Float, y: Float) = position += DenseVector(x, y)

  def setScale(x: Float, y: Float) = scale := DenseVector(x, y)

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

  def apply(): Transform = Transform(DenseVector(0f, 0f), DenseVector(0f, 0f))

  def apply(position: DenseVector[Float]): Transform = Transform(position, DenseVector(0f, 0f))

}
