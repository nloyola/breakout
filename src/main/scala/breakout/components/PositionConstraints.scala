package breakout.components

import breakout.entities.Entity

case class PositionConstraints(entity: Entity, left: Float, right: Float, top: Float, bottom: Float)
    extends Component {

  protected val typeName = "positionConstraints"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    val xPos        = entity.transform.position(0)
    val rightMargin = right - entity.transform.scale(0)

    if (xPos > rightMargin) {
      entity.transform.position(0) = rightMargin
    }

    if (xPos < left) {
      entity.transform.position(0) = left
    }

    val yPos         = entity.transform.position(1)
    val bottomMargin = bottom - entity.transform.scale(1)

    if (yPos > bottomMargin) {
      entity.transform.position(1) = bottomMargin
    }

    if (yPos < top) {
      entity.transform.position(1) = top
    }
    ()
  }

}
