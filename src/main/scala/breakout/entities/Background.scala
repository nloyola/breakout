package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg.DenseVector

case class Background(private val _width: Float, private val _height: Float, protected val _zIndex: Int)
    extends Entity {

  val name = "Background"

  private lazy val tex = AssetPool.texture("assets/images/background.png")

  protected lazy val _transform = Transform()

  override def posOffset(x: Float, y: Float): DenseVector[Float] = {
    throw new Error("background should not move")
  }

  addComponent(SpriteRenderer(entity = this, sprite = Sprite(tex)))

}
