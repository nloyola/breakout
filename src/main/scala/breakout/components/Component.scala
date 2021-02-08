package breakout.components

import breakout.entities.Entity
import play.api.libs.json._
//import scala.reflect.runtime.{ universe => ru }

trait Component {

  protected val typeName: String

  protected var _entity: Option[Entity] = None

  def entity = _entity

  def entity_=(obj: Entity) = _entity = Some(obj)

  def start(): Unit

  def update(dt: Float): Unit = {}

}

object Component {

  implicit val componentFormat: Format[Component] = new Format[Component] {

    override def writes(c: Component): JsValue = {
      val objJson = c match {
        case sc: SpriteRenderer => Json.toJson(sc)
        case rb: RigidBody      => Json.toJson(rb)
        case _ => JsNull
      }

      Json.obj("typeName" -> c.typeName) ++ objJson.as[JsObject]
    }

    override def reads(json: JsValue): JsResult[Component] =
      (json \ "typeName") match {
        case JsDefined(JsString("spriteRenderer")) => json.validate[SpriteRenderer]
        case JsDefined(JsString("rigidBody"))      => json.validate[RigidBody]
        case _                                     => JsError("error")
      }
  }
}
