package breakout.scenes

import breakout.gameobjects.{ Background, Paddle }
import breakout.util.AssetPool
import breakout.Camera
import breakout.gameobjects.AbstractGameObject
import org.joml.{ Vector2f }
import org.slf4j.LoggerFactory
import scala.collection.mutable.ArrayBuffer

class BreakoutScene extends Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val width = 1280f

  private val height = 720f

  protected val _camera = new Camera(new Vector2f(0f, 0f), width, height)

  private val objs = ArrayBuffer.empty[AbstractGameObject]

  // private var sprites: Option[Spritesheet] = None

  def init(): Unit = {
    logger.debug("init")
    loadResources()

    if (levelLoaded) {
      activeGameObject = Some(gameObjects(0))
    } else {

      objs += new Paddle(width, height, 1)
      objs += new Background(width, height, -10)

      objs.foreach(addGameObjectToScene)
      activeGameObject = Some(objs(0))
    }
  }

  def update(dt: Float): Unit = {
    // val tex = AssetPool.getTexture("assets/images/blendImage2.png")
    // tex.debugTexture(0f, 0f, 1000f, 1000f)

    gameObjects.foreach(_.update(dt))
    renderer.render()
  }

  private def loadResources(): Unit = {
    AssetPool.shader("assets/shaders/default.glsl")
    ()
  }

}
