package breakout.components

import breakout.renderers.Texture
import breeze.linalg.DenseVector
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class Spritesheet(
    val texture:      Texture,
    val spriteWidth:  Int,
    val spriteHeight: Int,
    val numSprites:   Int,
    val spacing:      Int) {
  val sprites = ArrayBuffer.empty[Sprite]

  private val logger = LoggerFactory.getLogger(this.getClass)

  def getSprite(index: Int): Sprite = sprites(index)

  private def init(): Unit = {
    var currentX = 0
    var currentY = texture.height - spriteHeight

    logger.debug(s"init: texHeight: ${texture.height}, spriteHeight: $spriteHeight")

    (0 until numSprites).foreach { i =>
      val topY    = (currentY + spriteHeight) / texture.height.toFloat
      val rightX  = (currentX + spriteWidth) / texture.width.toFloat
      val leftX   = currentX / texture.width.toFloat
      val bottomY = currentY / texture.height.toFloat

      val texCoords = Array(DenseVector(rightX, topY),
                            DenseVector(rightX, bottomY),
                            DenseVector(leftX, bottomY),
                            DenseVector(leftX, topY)
      )

      //logger.debug(s"init: $leftX, $topY, $leftX, $bottomY")

      val sprite = Sprite(spriteWidth, spriteHeight, Some(texture), texCoords)
      sprites += sprite

      currentX = currentX + spriteWidth + spacing
      if (currentX >= texture.width) {
        currentX = 0
        currentY -= spriteHeight + spacing
      }
    }
    logger.debug(s"init: added ${sprites.size} sprites")
  }

  def size(): Int = sprites.size

  init()
}
