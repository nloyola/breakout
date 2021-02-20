package breakout

import breakout.entities.{ Ball, Entity }
import breeze.linalg._
import org.slf4j.LoggerFactory

trait Direction {
  val dir: DenseVector[Float]
}

case class North() extends Direction {
  val dir = DenseVector(0f, 1f)
}

case class East() extends Direction {
  val dir = DenseVector(1f, 0f)
}

case class South() extends Direction {
  val dir = DenseVector(0f, -1f)
}

case class West() extends Direction {
  val dir = DenseVector(-1f, 0f)
}

trait CollisionResult

case class Collision(direction: Direction, intersectionAmount: DenseVector[Float]) extends CollisionResult

case class NoCollision() extends CollisionResult

object CollisionDetection {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val compass = Array[Direction](North(), East(), South(), West())

  def vectorDirection(v: DenseVector[Float]): Direction = {
    val n = normalize(v)
    compass.map(c => (c, n.dot(c.dir))).maxBy(_._2)._1
  }

  /**
   * Collision detection using Axis Aligned Bounding Boxes.
   *
   * @param entityA An entity to check for collision
   * @param entityB An entity to check for collision
   * @return true if the two entities collide.
   */
  def checkCollision(entityA: Entity, entityB: Entity): Boolean = {
    val collisionX = entityA.position(0) + entityA.scale(0) >= entityB.position(0) &&
      entityB.position(0) + entityB.scale(0) >= entityA.position(0)

    val collisionY = entityA.position(1) + entityA.scale(1) >= entityB.position(1) &&
      entityB.position(1) + entityB.scale(1) >= entityA.position(1)

    val result = collisionX && collisionY
    logger.debug(s"checkCollision: $result")
    result
  }

  /**
   * collision detection for a Circle and an  Axis Aligned Bounding Box.
   *
   * @param ball The ball is a circular object
   * @param entity An entity to check for collision
   * @return true if the two entities collide.
   */
  def checkCollision(ball: Ball, entity: Entity): CollisionResult = {
    def limit(x: Float, min: Float, max: Float): Float = if (x < min) min else if (x > max) max else x

    val center          = ball.position + DenseVector(ball.radius, ball.radius)
    val aabbHalfExtents = entity.scale / 2f
    val aabbCenter      = entity.position + aabbHalfExtents
    val difference      = center - aabbCenter
    val clamped         = DenseVector(limit(difference(0), -aabbHalfExtents(0), aabbHalfExtents(0)),
                              limit(difference(1), -aabbHalfExtents(1), aabbHalfExtents(1))
    )
    val closest         = aabbCenter + clamped - center

    if (norm(closest) <= ball.radius) {
      logger.trace(s"checkCollision: ${vectorDirection(closest)}, diff: $closest")
      Collision(vectorDirection(closest), closest)
    } else {
      NoCollision()
    }
  }
}
