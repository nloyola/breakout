package breakout.entities

import breakout.Transform

case class GameObject(name: String, _transform: Transform, _zIndex: Int) extends Entity {}

object GameObject {

  def apply(name: String): GameObject = GameObject(name, Transform(), 0)

  def apply(name: String, transform: Transform, zIndex: Int): GameObject = GameObject(name, transform, zIndex)

}
