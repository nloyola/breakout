package breakout.games

import breakout.components.PositionConstraints
import breakout.entities.{ Background, Ball, Block, BlockBreakable, Paddle }
import breakout.scenes.Scene
import breakout.{ Collision, CollisionDetection, East, KeyListener, North, South, West }
import breeze.linalg._
import org.lwjgl.glfw.GLFW.{ GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_SPACE }
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

trait GameState

case class GameMenu() extends GameState

case class GameActive() extends GameState

case class GameWin() extends GameState

class BreakoutGame(scene: Scene, width: Float, height: Float) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val paddleAbsVelocity = 500f

  private val ballInitialVelocity = DenseVector(100f, -350f, 0f)

  private val levels = ArrayBuffer.empty[GameLevel]

  private val level = 0

  // private var state = GameMenu()

  private val paddleWidth = width / 8f

  private val paddleHeight = height / 15f

  private val paddle: Paddle = Paddle(1)

  private val ball = Ball(width / 100f, 3)

  private val particleGenerator = new ParticleGenerator(scene)

  def init(): Unit = {
    logger.debug("init")
    val background = Background(-10)
    background.scale = (width, height)

    paddle.position := DenseVector(0f, height - paddleHeight)
    paddle.scale = (paddleWidth, paddleHeight)
    paddle.addComponent(
      PositionConstraints(entity = paddle, left = 0, right = width, top = 0, bottom = height)
    )

    updateBall()

    scene.addEntityToScene(background)
    scene.addEntityToScene(paddle)
    scene.addEntityToScene(ball)

    val levelOne = new GameLevel()
    levelOne.load(GameLevel.levelOne, width, height / 2f)

    levels += levelOne
    levelOne.blocks.foreach(scene.addEntityToScene)

    particleGenerator.init(2)

    ()
  }

  def update(dt: Float): Unit = {
    val velocity =
      if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
        paddleAbsVelocity
      } else if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
        -paddleAbsVelocity
      } else {
        0
      }

    if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
      if (ball.stuck) {
        ball.stuck = false
        ball.velocity = ballInitialVelocity
      }
    }

    paddle.velocityX = velocity

    updateBall()
    particleGenerator.update(dt, ball, 2)
    doCollisions()

    val brokenBlocks = scene.entities.collect { case b: BlockBreakable => b }.filter(_.destroyed)
    brokenBlocks.foreach { block =>
      scene.removeEntity(block)
      logger.debug(s"bock removed: pos: ${block.position}")
    }

    // did ball go past the bottom?
    if (ball.position(1) >= height) {
      resetLevel()
      resetPlayer()
    }
  }

  private def updateBall(): Unit = {
    logger.trace(s"ball velocity: ${ball.velocity}")

    if (ball.stuck) {
      ball.position := paddle.position + DenseVector(paddle.scale(0) / 2f - ball.radius,
                                                     -paddle.scale(1) / 2f - ball.radius / 2f + 3
      )
      ball.velocity := DenseVector.zeros[Float](3)
    } else {
      val xPos        = ball.position(0)
      val rightMargin = width - ball.radius

      if ((xPos > rightMargin) || (xPos < 0)) {
        ball.velocity := DenseVector(-ball.velocity(0), ball.velocity(1), 0f)
      }

      val yPos = ball.position(1)
      //val bottomMargin = height - ball.radius

      if (yPos < 0) {
        ball.velocity = DenseVector(ball.velocity(0), -ball.velocity(1), 0f)
      }
    }
    ()
  }

  private def doCollisions(): Unit = {
    levels(level).blocks.foreach { block =>
      val checkBlock = block match {
        case b: BlockBreakable => !b.destroyed
        case _ => true
      }

      if (checkBlock) {
        CollisionDetection.checkCollision(ball, block) match {
          case Collision(direction, amount) =>
            block match {
              case b: BlockBreakable =>
                b.destroyed = true
                logger.debug(s"broken block: pos: ${b.position}")
              case _ =>
              //do nothing
            }

            direction match {
              case _: East | _: West =>
                ball.velocityX = -ball.velocityX
                val penetration = ball.radius - amount(0).abs

                direction match {
                  case _: East => ball.posOffset(penetration, 0)
                  case _ => ball.posOffset(-penetration, 0)
                }

              case _: North | _: South =>
                ball.velocityY = -ball.velocityY
                val penetration = ball.radius - amount(1).abs

                direction match {
                  case _: North => ball.posOffset(0, -penetration)
                  case _ => ball.posOffset(0, penetration)
                }

              case _ => // do nothing
            }
          case _                            => // do nothing
        }
      }
    }

    if (!ball.stuck) {
      CollisionDetection.checkCollision(ball, paddle) match {
        case Collision(direction, amount) =>
          val centerPaddle = paddle.position(0) + paddle.scale(0) / 2f
          val distance     = ball.position(0) + ball.radius - centerPaddle
          val percentage   = 2f * distance / paddle.scale(0)
          val strength     = 2f
          val magnitude    = norm(ball.velocity).toFloat
          val vX           = ballInitialVelocity(0) * percentage * strength
          val vY           = -1f * ball.velocity(1).abs
          val newVel       = normalize(DenseVector(vX, vY, 0)) * magnitude

          ball.velocity := newVel

          logger.trace(
            s"direction: $direction, amount: $amount, paddle pos: ${paddle.position}, ball pos: ${ball.position}, ball vel: ${ball.velocity}"
          )
        case _                            => // do nothing
      }
    }
  }

  private def resetLevel(): Unit = {
    scene.entities.collect { case b: Block => b }.foreach(scene.removeEntity)

    val levelOne = new GameLevel()
    levelOne.load(GameLevel.levelOne, width, height / 2f)
    levelOne.blocks.foreach(scene.addEntityToScene)
    levels(level) = levelOne

    ball.stuck = true
    ball.position := DenseVector(paddle.width / 2f, height - paddle.height - ball.height - 0.01f)
    ()
  }

  private def resetPlayer(): Unit = {
    paddle.position := DenseVector(0, height - paddleHeight)
    ()
  }

}
