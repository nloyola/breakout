package breakout.components

case class PositionConstraints(left: Float, right: Float, top: Float, bottom: Float) extends Component {

  protected val typeName = "positionConstraints"

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    _entity.map { e =>
      val xPos        = e.transform.position.x
      val rightMargin = right - e.transform.scale.x

      if (xPos > rightMargin) {
        e.transform.position.x = rightMargin
      }

      if (xPos < left) {
        e.transform.position.x = left
      }

      val yPos         = e.transform.position.y
      val bottomMargin = bottom - e.transform.scale.y

      if (yPos > bottomMargin) {
        e.transform.position.y = bottomMargin
      }

      if (yPos < top) {
        e.transform.position.y = top
      }
    }
    ()
  }

}
