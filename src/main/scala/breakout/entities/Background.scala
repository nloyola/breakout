package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg.DenseVector

case class Background(zIndex: Int) extends Entity {

  val name = "Background"

  private lazy val tex = AssetPool.texture("assets/images/background.png")

  val transform = Transform()

  override def position_=(pos: DenseVector[Float]) = {
    throw new Error("background should not move")
  }

  override def posOffset(x: Float, y: Float): DenseVector[Float] = {
    throw new Error("background should not move")
  }

  addComponent(SpriteRenderer(entity = this, sprite = Sprite(tex)))

}
