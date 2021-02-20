package breakout.components

import breakout.entities.Entity

trait Component {

  val entity: Entity

  protected val typeName: String

  def start(): Unit

  def update(dt: Float): Unit = {}

}
