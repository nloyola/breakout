package breakout.renderers

import breakout.entities.Entity
import breakout.components.SpriteRenderer
//import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class Renderer {
  //private val logger         = LoggerFactory.getLogger(this.getClass)
  private val MAX_BATCH_SIZE = 1000
  private var batches        = ArrayBuffer.empty[RenderBatch]

  def add(obj: Entity): Unit = {
    obj.component[SpriteRenderer]().foreach(add)
  }

  private def add(sprite: SpriteRenderer): Unit = {
    val found = for {
      go    <- sprite.gameObject
      tex   <- sprite.texture()
      batch <- batches.find { batch =>
                 batch.hasRoom() && (batch.getZIndex() == go.zIndex) && (batch.hasTextureRoom() || batch
                   .hasTexture(tex))
               }
    } yield batch

    found match {
      case Some(batch) => batch.addSprite(sprite)
      case None        =>
        val newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.map(_.zIndex).getOrElse(0))
        newBatch.start()
        batches += newBatch
        newBatch.addSprite(sprite)
        batches = batches.sorted
    }
  }

  def render(): Unit = batches.foreach(_.render())
}
