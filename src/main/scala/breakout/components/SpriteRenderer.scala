package breakout.components

import breakout._
import breakout.renderers.Texture
import org.joml.Vector2f
import org.joml.Vector4f
import org.slf4j.LoggerFactory
import play.api.libs.json._

case class SpriteRenderer(sprite: Sprite, color: Vector4f) extends Component {

  protected val log      = LoggerFactory.getLogger(this.getClass)
  protected val typeName = "spriteRenderer"

  var gameObject:    Option[GameObject] = None
  var lastTransform: Option[Transform]  = None
  var _isDirty = true

  override def start(): Unit = {
    lastTransform = gameObject.map(go => go.transform.copy())
  }

  override def update(dt: Float): Unit = {
    for {
      lt <- lastTransform
      go <- gameObject
    } yield {
      if (!lt.equals(go.transform)) {
        log.info(s"last transform: $lt")
        gameObject = Some(go.copy(transform = lt.copy()))
        _isDirty = true
      }
    }
    ()
  }

  def texture(): Option[Texture] = sprite.texture

  def texCoords(): List[Vector2f] = sprite.texCoords

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

  def apply(sprite: Sprite): SpriteRenderer = SpriteRenderer(sprite, new Vector4f(1, 1, 1, 1))

  def apply(color: Vector4f): SpriteRenderer = SpriteRenderer(Sprite(None), color)

  implicit val spriteRendererFormat: Format[SpriteRenderer] = Json.format[SpriteRenderer]

}
