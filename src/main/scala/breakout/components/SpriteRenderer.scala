package breakout.components

import breakout._
import breakout.renderers.Texture
import org.joml.{ Vector2f, Vector4f }
import org.slf4j.LoggerFactory
import play.api.libs.json._

case class SpriteRenderer(sprite: Sprite, color: Vector4f) extends Component {

  protected val log      = LoggerFactory.getLogger(this.getClass)
  protected val typeName = "spriteRenderer"

  private var lastTransform: Option[Transform] = None
  private var _isDirty = true

  override def start(): Unit = {
    lastTransform = gameObject.map(_.transform.copy())
  }

  override def update(dt: Float): Unit = {
    for {
      lt  <- lastTransform
      obj <- gameObject
    } yield {
      if (!lt.equals(obj.transform)) {
        log.trace(s"last transform: $lt, obj transform: ${obj.transform}")
        lastTransform = Some(obj.transform.copy())
        _isDirty = true
      }
    }
    ()
  }

  def texture(): Option[Texture] = sprite.texture

  def texCoords(): Array[Vector2f] = sprite.texCoords

  def setColor(c: Vector4f): Unit = {
    if (!color.equals(c)) {
      color.set(c)
      _isDirty = true
    }
  }

  def isDirty(): Boolean = _isDirty

  def setClean(): Unit = _isDirty = false

  override def toString: String = s"gameObject: $gameObject, isDirty: ${_isDirty})"

}

object SpriteRenderer {

  def apply(sprite: Sprite): SpriteRenderer =
    SpriteRenderer(sprite = sprite, color = new Vector4f(1, 1, 1, 1))

  def apply(color: Vector4f): SpriteRenderer = SpriteRenderer(Sprite(), color)

  implicit val spriteRendererFormat: Format[SpriteRenderer] = Json.format[SpriteRenderer]

}
