package breakout.entities

import breakout.Transform
import breakout.components.{ RigidBody, Sprite, SpriteRenderer }
import breakout.util.AssetPool
import org.joml.{ Vector2f, Vector3f, Vector4f }
import play.api.libs.json._

trait Block extends Entity {

  protected lazy val _transform = Transform(new Vector2f(), new Vector2f())

  protected val rigidBody = RigidBody(this, 1, new Vector3f(), 0f)

}

case class BlockSolid(protected val _zIndex: Int) extends Block() {

  override val name = "BlockSolid"

  private val spriteRenderer = SpriteRenderer(entity = this, sprite = BlockSolid.sprite)

  addComponent(spriteRenderer)
  addComponent(rigidBody)
}

object BlockSolid {

  val sprite = Sprite(AssetPool.texture("assets/images/block_solid.png"))

  implicit val blockFormat: Format[BlockSolid] = Json.format[BlockSolid]

}

case class BlockBreakable(protected val color: Vector4f, protected val _zIndex: Int) extends Block {

  val name = "BlockBreakable"

  private val renderer = SpriteRenderer(entity = this, sprite = BlockBreakable.sprite)

  private var _destroyed = false

  def destroyed = _destroyed

  def destroyed_=(d: Boolean) = _destroyed = d

  renderer.setColor(color)

  addComponent(renderer)
  addComponent(rigidBody)

}

object BlockBreakable {
  import breakout._

  val sprite = Sprite(AssetPool.texture("assets/images/block.png"))

  implicit val blockFormat: Format[BlockBreakable] = Json.format[BlockBreakable]

}
