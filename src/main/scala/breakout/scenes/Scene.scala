package breakout.scenes

import breakout.Camera
import breakout.entities.Entity
import breakout.renderers.Renderer
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

trait Scene {

  private val logger = LoggerFactory.getLogger(this.getClass)

  protected val renderer = new Renderer
  protected val _camera: Camera
  protected var isRunning = false
  protected val _entities = ArrayBuffer.empty[Entity]
  protected var activeEntity: Option[Entity] = None
  protected var levelLoaded = false

  def entities = _entities

  def init(): Unit

  def update(deltaTime: Float): Unit

  def start(): Unit = {
    _entities.foreach { entity =>
      entity.start()
      renderer.add(entity)
    }
    isRunning = true
  }

  def addEntityToScene(entity: Entity): Unit = {
    logger.debug(s"adding entity $entity")
    entities += entity
    if (isRunning) {
      entity.start()
      renderer.add(entity)
    }
  }

  def removeEntity(entity: Entity): Unit = {
    _entities -= entity
    if (isRunning) {
      renderer.destroyEntity(entity)
    }
  }

  def camera: Camera = _camera

}
