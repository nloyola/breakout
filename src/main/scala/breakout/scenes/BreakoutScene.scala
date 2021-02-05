package breakout.scenes

import breakout.components._
import breakout.gameobjects.Paddle
import breakout.util.AssetPool
import breakout.Camera
import breakout.gameobjects.AbstractGameObject
import org.joml.{ Vector2f }
import org.slf4j.LoggerFactory

class BreakoutScene extends Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val width = 1280f

  private val height = 720f

  protected val _camera = new Camera(new Vector2f(0f, 0f), width, height)

  private var obj1: AbstractGameObject = null

  // private var sprites: Option[Spritesheet] = None

  def init(): Unit = {
    logger.debug("init")
    loadResources()

    if (levelLoaded) {
      activeGameObject = Some(gameObjects(0))
    } else {

      obj1 = new Paddle(1)
      addGameObjectToScene(obj1)
      activeGameObject = Some(obj1)

      // val obj2 = GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1)
      //obj2.addComponent(new SpriteRenderer(sheet.getSprite(7)))
      // obj2.addComponent(SpriteRenderer(Sprite(Some(AssetPool.getTexture("assets/images/blendImage2.png")))))
      // addGameObjectToScene(obj2)
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

    val assetName = "assets/images/paddle.png"
    AssetPool.addSpritesheet(assetName, new Spritesheet(AssetPool.texture(assetName), 16, 16, 81, 0));

    ()
  }

}
