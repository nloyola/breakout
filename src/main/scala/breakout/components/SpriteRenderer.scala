package breakout.components

import breakout._
import breakout.entities.Entity
import breakout.renderers.Texture
import org.joml.{ Vector2f, Vector4f }
import org.slf4j.LoggerFactory
import play.api.libs.json._

case class SpriteRenderer(entity: Entity, sprite: Sprite, color: Vector4f) extends Component {

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

  def texCoords: Array[Vector2f] = sprite.texCoords

  def setColor(c: Vector4f): Unit = {
    if (!color.equals(c)) {
      color.set(c)
      _isDirty = true
    }
  }

  def setColor(r: Float, g: Float, b: Float, a: Float): Unit = {
    if (!color.equals(r, g, b, a)) {
      color.set(r, g, b, a)
      _isDirty = true
    }
  }

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
    SpriteRenderer(entity = entity, sprite = sprite, color = new Vector4f(1, 1, 1, 1))

  def apply(entity: Entity, color: Vector4f): SpriteRenderer = SpriteRenderer(entity, Sprite(), color)

  implicit val spriteRendererFormat: Format[SpriteRenderer] = Json.format[SpriteRenderer]

}
