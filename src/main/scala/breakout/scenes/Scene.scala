package breakout.scenes

import imgui.ImGui;
import java.io._
import java.nio.file.{ Files, Paths }
import breakout.{ Camera, GameObject }
import breakout.renderers.Renderer
import play.api.libs.json._
import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import scala.io.Source

trait Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  protected val renderer = new Renderer
  protected val camera: Camera
  protected var isRunning   = false
  protected val gameObjects = ListBuffer.empty[GameObject]
  protected var activeGameObject: Option[GameObject] = None
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

  def addGameObjectToScene(obj: GameObject): Unit = {
    gameObjects += obj
    if (isRunning) {
      obj.start()
      renderer.add(obj)
    }
  }

  def getCamera(): Camera = camera

  def sceneImgui(): Unit = {
    activeGameObject.foreach { go =>
      ImGui.begin("Instpector")
      go.imgui();
      ImGui.end
    }
    imgui()
  }

  def imgui(): Unit = {}

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
        Json.parse(contents).validate[ListBuffer[GameObject]] match {
          case e:    JsError                           => logger.error(s"could not read level data: $e")
          case objs: JsSuccess[ListBuffer[GameObject]] =>
            gameObjects.clear()
            objs.value.foreach { go =>
              addGameObjectToScene(go)
              go.components.foreach(_.gameObject = Some(go))
            }
            levelLoaded = true
        }
      }
    }
  }

}
