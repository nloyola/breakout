package breakout.games

import breakout.entities.{ Block, BlockBreakable, BlockSolid }
import org.joml.Vector4f
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class GameLevel {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val _blocks = ArrayBuffer.empty[Block]

  def blocks = _blocks

  def load(levelAsStr: String, levelWidth: Float, levelHeight: Float): Unit = {
    _blocks.clear()

    val blockData = levelAsStr.split('\n').map { line =>
      line.split(' ').map(_.toInt).toArray
    }

    if (blockData.length <= 0) {
      throw new Error("could not parse level string")
    }

    init(blockData, levelWidth, levelHeight)
  }

  def isComplete(): Boolean = false

  private def init(blockData: Array[Array[Int]], levelWidth: Float, levelHeight: Float): Unit = {
    val height      = blockData.size
    val width       = blockData(0).size
    val blockHeight = levelHeight / height.toFloat
    val blockWidth  = levelWidth / width.toFloat

    (0 until height).foreach { row =>
      (0 until width).foreach { col =>
        val block = blockData(row)(col) match {
          case 0 => None

          case 1 => Some(BlockSolid(1))

          case u =>
            val color = u match {
              case 2 => new Vector4f(0.2f, 0.6f, 1.0f, 1f)
              case 3 => new Vector4f(0.0f, 0.7f, 0.0f, 1f)
              case 4 => new Vector4f(0.8f, 0.8f, 0.4f, 1f)
              case 5 => new Vector4f(1.0f, 0.5f, 0.0f, 1f)
            }

            Some(BlockBreakable(color, 1))
        }

        block.map { b =>
          b.posOffset(blockWidth * col, blockHeight * row)
          b.scale(blockWidth, blockHeight)
          _blocks += b

          logger.trace(s"$row, $col, ${blockWidth * col}, ${blockHeight * row}, $blockWidth, $blockHeight")
        }
      }
    }
  }
}

object GameLevel {

  // val levelOne: String =
  //   s"""|2 3 2
  //       |""".stripMargin

  val levelOne: String =
    s"""|1 1 1 1 1 1
        |2 2 0 0 2 2
        |3 3 4 4 3 3
        |""".stripMargin
}
