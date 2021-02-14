package breakout.renderers

import breakout.components.SpriteRenderer
import breakout.entities.Entity

import scala.collection.mutable.ArrayBuffer

class Renderer {
  //private val logger         = LoggerFactory.getLogger(this.getClass)
  private val MAX_BATCH_SIZE = 1000
  private var batches        = ArrayBuffer.empty[RenderBatch]

  def add(entity: Entity): Unit = {
    entity.component[SpriteRenderer]().foreach(add)
  }

  def render(): Unit = batches.foreach(_.render())

  def destroyEntity(entity: Entity): Unit = {
    entity.component[SpriteRenderer]().foreach { sr =>
      batches.foreach { _.destroyEntity(entity) }
    }
  }

  private def add(sprite: SpriteRenderer): Unit = {
    val found = for {
      texture <- sprite.texture
      batch   <- batches.find { batch =>
                   batch.hasRoom() && (batch.getZIndex() == sprite.entity.zIndex) &&
                   (batch.hasTextureRoom() || batch.hasTexture(texture))
                 }
    } yield batch

    found match {
      case Some(batch) =>
        batch.addSprite(sprite)
      case None        =>
        val newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.entity.zIndex)
        newBatch.start()
        batches += newBatch
        newBatch.addSprite(sprite)
        batches = batches.sorted
    }
  }
}
