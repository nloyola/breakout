package breakout.components

import breakout.entities.Entity

case class PositionConstraints(entity: Entity, left: Float, right: Float, top: Float, bottom: Float)
    extends Component {

  protected val typeName = "positionConstraints"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    val xPos        = entity.transform.position.x
    val rightMargin = right - entity.transform.scale.x

    if (xPos > rightMargin) {
      entity.transform.position.x = rightMargin
    }

    if (xPos < left) {
      entity.transform.position.x = left
    }

    val yPos         = entity.transform.position.y
    val bottomMargin = bottom - entity.transform.scale.y

    if (yPos > bottomMargin) {
      entity.transform.position.y = bottomMargin
    }

    if (yPos < top) {
      entity.transform.position.y = top
    }
    ()
  }

}
