package breakout.components

import breakout._
import breakout.entities.Entity
import breakout.renderers.Texture
import breeze.linalg.DenseVector
import org.slf4j.LoggerFactory

case class SpriteRenderer(entity: Entity, sprite: Sprite, color: DenseVector[Float]) extends Component {

  protected val log = LoggerFactory.getLogger(this.getClass)

  protected val typeName = "spriteRenderer"

  private var lastTransform: Transform = entity.transform.copy()

  private var _isDirty = true

  override def start(): Unit = {}

  override def update(dt: Float): Unit = {
    if (!lastTransform.equals(entity.transform)) {
      log.trace(s"last transform: $lastTransform, obj transform: ${entity.transform}")
      lastTransform = entity.transform.copy()
      _isDirty = true
    }
    ()
  }

  def texture: Option[Texture] = sprite.texture

  def texCoords: Array[DenseVector[Float]] = sprite.texCoords

  def isDirty: Boolean = _isDirty

  def setClean(): Unit = _isDirty = false

  def setDirty(): Unit = _isDirty = true

  override def equals(that: Any): Boolean = {
    that match {
      case sr: SpriteRenderer => entity == sr.entity
      case _ => false
    }
  }

  override def hashCode: Int = 41 * entity.hashCode + sprite.hashCode

  override def toString: String = s"entity: $entity, isDirty: ${_isDirty})"

}

object SpriteRenderer {

  def apply(entity: Entity, sprite: Sprite): SpriteRenderer =
    SpriteRenderer(entity = entity, sprite = sprite, color = DenseVector(1f, 1f, 1f, 1f))

  def apply(entity: Entity, color: DenseVector[Float]): SpriteRenderer =
    SpriteRenderer(entity, Sprite(), color)

}
