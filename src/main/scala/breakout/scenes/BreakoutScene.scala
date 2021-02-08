package breakout.scenes

import breakout.games.BreakoutGame
import breakout.util.AssetPool
import breakout.Camera
import org.joml.{ Vector2f }
import org.slf4j.LoggerFactory

class BreakoutScene extends Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val width = 1280f

  private val height = 720f

  protected val _camera = new Camera(new Vector2f(0f, 0f), width, height)

  private val game = new BreakoutGame(width, height)

  def init(): Unit = {
    logger.debug("init")
    loadResources()

    if (levelLoaded) {
      activeEntity = Some(entities(0))
    } else {

      game.entities.foreach(addEntityToScene)
    }
  }

  def update(dt: Float): Unit = {
    // val tex = AssetPool.getTexture("assets/images/blendImage2.png")
    // tex.debugTexture(0f, 0f, 1000f, 1000f)

    entities.foreach(_.update(dt))
    renderer.render()
  }

  private def loadResources(): Unit = {
    AssetPool.shader("assets/shaders/default.glsl")
    ()
  }

}
