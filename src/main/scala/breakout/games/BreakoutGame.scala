package breakout.games

import breakout.components.PositionConstraints
import breakout.entities.{ Background, Ball, BlockBreakable, Entity, Paddle }
import breakout.{ Collision, CollisionDetection, East, KeyListener, North, South, West }
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.{ GLFW_KEY_A, GLFW_KEY_D }

import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory

trait GameState

case class GameMenu() extends GameState

case class GameActive() extends GameState

case class GameWin() extends GameState

class BreakoutGame(width: Float, height: Float) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val paddleAbsVelocity = 500f

  private val ballInitialVelocity = new Vector2f(100f, -350f)

  private val _entities = ArrayBuffer.empty[Entity]

  private val levels = ArrayBuffer.empty[GameLevel]

  private val level = 0

  // private var state = GameMenu()

  private val paddleWidth = width / 8f

  private val paddleHeight = height / 15f

  private val paddle: Paddle = Paddle(paddleWidth, paddleHeight, 1)

  // FIXME: change zIndex back to 1 after collision detection is done
  private val ball = Ball(width / 100f, 2)

  def entities = _entities

  def update(dt: Float): Unit = {
    val velocity =
      if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
        paddleAbsVelocity
      } else if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
        -paddleAbsVelocity
      } else {
        0
      }

    paddle.velocityX = velocity
    updateBall()
    doCollisions()
  }

  private def init(): Unit = {
    val background = Background(width, height, -10)

    paddle.posOffset(0, height - paddleHeight)
    paddle.addComponent(PositionConstraints(0, width, 0, height))

    ball.posOffset(paddle.width / 2, height - paddle.height - ball.height - 0.01f)
    //ball.addComponent(PositionConstraints(0, width, 0, height))
    ball.velocity = ballInitialVelocity

    background.scale(width, height)

    val levelOne = new GameLevel()
    levelOne.load(GameLevel.levelOne, width, height / 2f)

    levels += levelOne

    _entities += background
    _entities += paddle
    _entities += ball
    _entities ++= levelOne.blocks
  }

  private def updateBall(): Unit = {
    val xPos        = ball.position.x
    val rightMargin = width - ball.radius

    if ((xPos > rightMargin) || (xPos < 0)) {
      ball.velocity = new Vector2f(-ball.velocity.x, ball.velocity.y)
    }

    val yPos = ball.position.y
    //val bottomMargin = height - ball.radius

    if (yPos < 0) {
      ball.velocity = new Vector2f(ball.velocity.x, -ball.velocity.y)
    }
  }

  private def doCollisions(): Unit = {
    levels(level).blocks.foreach { block =>
      CollisionDetection.checkCollision(ball, block) match {
        case Collision(direction, amount) =>
          block match {
            case b: BlockBreakable => b.destroyed = true
            case _ => //do nothing
          }

          direction match {
            case _: East | _: West =>
              ball.velocityX = -ball.velocityX
              val penetration = ball.radius - amount.x.abs

              direction match {
                case _: East => ball.posOffset(penetration, 0)
                case _ => ball.posOffset(-penetration, 0)
              }

            case _: North | _: South =>
              ball.velocityY = -ball.velocityY
              val penetration = ball.radius - amount.y.abs

              direction match {
                case _: North => ball.posOffset(0, -penetration)
                case _ => ball.posOffset(0, penetration)
              }

            case _ => // do nothing
          }
        case _                            => // do nothing
      }
    }

    CollisionDetection.checkCollision(ball, paddle) match {
      case Collision(direction, amount) =>
        val centerPaddle = paddle.position.x + paddle.scale.x / 2f
        val distance     = ball.position.x + ball.radius - centerPaddle
        val percentage   = 2f * distance / paddle.scale.x
        val strength     = 2f
        val prevVelocity = new Vector2f(ball.velocity.x, ball.velocity.y)
        val vX           = ballInitialVelocity.x * percentage * strength
        val vY           = -ball.velocity.y
        val newVel       = new Vector2f(vX, vY).normalize().mul(prevVelocity.length())
        ball.velocityX = newVel.x
        ball.velocityY = newVel.y

        logger.trace(
          s"direction: $direction, amount: $amount, paddle pos: ${paddle.position}, ball pos: ${ball.position}, ball vel: ${ball.velocity}"
        )
      case _                            => // do nothing
    }
  }

  init()

}
