package breakout.renderers

import breakout.entities.Entity
import breakout.components.SpriteRenderer
//import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class Renderer {
  //private val logger         = LoggerFactory.getLogger(this.getClass)
  private val MAX_BATCH_SIZE = 1000
  private var batches        = ArrayBuffer.empty[RenderBatch]

  def add(entity: Entity): Unit = {
    entity.component[SpriteRenderer]().foreach(add)
  }

  private def add(sprite: SpriteRenderer): Unit = {
    val found = for {
      entity  <- sprite.entity
      texture <- sprite.texture
      batch   <- batches.find { batch =>
                   batch.hasRoom() && (batch.getZIndex() == entity.zIndex) && (batch.hasTextureRoom() || batch
                     .hasTexture(texture))
                 }
    } yield batch

    found match {
      case Some(batch) =>
        batch.addSprite(sprite)
      case None        =>
        val newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.entity.map(_.zIndex).getOrElse(0))
        newBatch.start()
        batches += newBatch
        newBatch.addSprite(sprite)
        batches = batches.sorted
    }
  }

  def render(): Unit = batches.foreach(_.render())
}
