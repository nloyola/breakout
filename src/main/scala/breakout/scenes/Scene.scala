package breakout.scenes

import breakout.renderers.Renderer
import breakout.Camera
import breakout.entities.Entity
import org.slf4j.LoggerFactory
import play.api.libs.json._

import java.io._
import java.nio.file.{ Files, Paths }
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

trait Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  protected val renderer = new Renderer
  protected val _camera: Camera
  protected var isRunning = false
  protected val entities  = ArrayBuffer.empty[Entity]
  protected var activeEntity: Option[Entity] = None
  protected var levelLoaded = false

  def init(): Unit

  def update(deltaTime: Float): Unit

  def start(): Unit = {
    entities.foreach { entity =>
      entity.start()
      renderer.add(entity)
    }
    isRunning = true
  }

  def addEntityToScene(obj: Entity): Unit = {
    entities += obj
    if (isRunning) {
      obj.start()
      renderer.add(obj)
    }
  }

  def camera: Camera = _camera

  def saveExit(): Unit = {
    val filename = "level.json"
    new File(filename).delete()
    val pw       = new PrintWriter(new File(filename))

    pw.write(Json.prettyPrint(Json.toJson(entities)))
    pw.close
  }

  def load(): Unit = {
    if (Files.exists(Paths.get("level.json"))) {
      val source   = Source.fromFile("level.json")
      val contents =
        try source.mkString
        finally source.close

      if (!contents.isEmpty) {
        Json.parse(contents).validate[ArrayBuffer[Entity]] match {
          case e:    JsError                        => logger.error(s"could not read level data: $e")
          case objs: JsSuccess[ArrayBuffer[Entity]] =>
            entities.clear()
            objs.value.foreach { obj =>
              addEntityToScene(obj)
              obj.components.foreach(_.entity = obj)
            }
            levelLoaded = true
        }
      }
    }
  }

}
