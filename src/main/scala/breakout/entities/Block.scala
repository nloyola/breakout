package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg._

trait Block extends Entity {

  protected lazy val _transform = Transform()

  protected val rigidBody = RigidBody(this, 1, 0f)

}

case class BlockSolid(protected val _zIndex: Int) extends Block() {

  override val name = "BlockSolid"

  private val spriteRenderer = SpriteRenderer(entity = this, sprite = BlockSolid.sprite)

  addComponent(spriteRenderer)
  addComponent(rigidBody)
}

object BlockSolid {

  val sprite = Sprite(AssetPool.texture("assets/images/block_solid.png"))

}

case class BlockBreakable(color: DenseVector[Float], _zIndex: Int) extends Block {

  val name = "BlockBreakable"

  private val renderer = SpriteRenderer(entity = this, sprite = BlockBreakable.sprite)

  private var _destroyed = false

  def destroyed = _destroyed

  def destroyed_=(d: Boolean) = _destroyed = d

  renderer.color := color

  addComponent(renderer)
  addComponent(rigidBody)

}

object BlockBreakable {

  val sprite = Sprite(AssetPool.texture("assets/images/block.png"))

}
