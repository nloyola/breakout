package breakout.components

import breakout.renderers.Texture
import org.joml.Vector2f;
import scala.collection.mutable.ListBuffer
import org.slf4j.LoggerFactory

class Spritesheet(
    val texture:      Texture,
    val spriteWidth:  Int,
    val spriteHeight: Int,
    val numSprites:   Int,
    val spacing:      Int) {
  val sprites = ListBuffer.empty[Sprite]

  private val logger = LoggerFactory.getLogger(this.getClass)

  def getSprite(index: Int): Sprite = sprites(index)

  private def init(): Unit = {
    var currentX = 0
    var currentY = texture.getHeight() - spriteHeight

    logger.debug(s"init: texHeight: ${texture.getHeight()}, spriteHeight: $spriteHeight")

    (0 until numSprites).foreach { i =>
      val topY    = (currentY + spriteHeight) / texture.getHeight().toFloat
      val rightX  = (currentX + spriteWidth) / texture.getWidth().toFloat
      val leftX   = currentX / texture.getWidth().toFloat
      val bottomY = currentY / texture.getHeight().toFloat

      val texCoords = List(new Vector2f(rightX, topY),
                           new Vector2f(rightX, bottomY),
                           new Vector2f(leftX, bottomY),
                           new Vector2f(leftX, topY)
      )

      //logger.debug(s"init: $leftX, $topY, $leftX, $bottomY")

      val sprite = Sprite(spriteWidth, spriteHeight, Some(texture), texCoords)
      sprites += sprite

      currentX = currentX + spriteWidth + spacing
      if (currentX >= texture.getWidth()) {
        currentX = 0
        currentY -= spriteHeight + spacing
      }
    }
    logger.debug(s"init: added ${sprites.size} sprites")
  }

  def size(): Int = sprites.size

  init()
}
