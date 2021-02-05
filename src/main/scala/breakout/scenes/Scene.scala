package breakout.scenes

import breakout.renderers.Renderer
import breakout.Camera
import breakout.gameobjects.AbstractGameObject
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
  protected var isRunning   = false
  protected val gameObjects = ArrayBuffer.empty[AbstractGameObject]
  protected var activeGameObject: Option[AbstractGameObject] = None
  protected var levelLoaded = false

  def init(): Unit

  def update(deltaTime: Float): Unit

  def start(): Unit = {
    gameObjects.foreach { obj =>
      obj.start()
      renderer.add(obj)
    }
    isRunning = true
  }

  def addGameObjectToScene(obj: AbstractGameObject): Unit = {
    gameObjects += obj
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

    pw.write(Json.prettyPrint(Json.toJson(gameObjects)))
    pw.close
  }

  def load(): Unit = {
    if (Files.exists(Paths.get("level.json"))) {
      val source   = Source.fromFile("level.json")
      val contents =
        try source.mkString
        finally source.close

      if (!contents.isEmpty) {
        Json.parse(contents).validate[ArrayBuffer[AbstractGameObject]] match {
          case e:    JsError                                    => logger.error(s"could not read level data: $e")
          case objs: JsSuccess[ArrayBuffer[AbstractGameObject]] =>
            gameObjects.clear()
            objs.value.foreach { obj =>
              addGameObjectToScene(obj)
              obj.components.foreach(_.gameObject = obj)
            }
            levelLoaded = true
        }
      }
    }
  }

}
