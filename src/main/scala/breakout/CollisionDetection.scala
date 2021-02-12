package breakout

import breakout.entities.{ Ball, Entity }
import org.joml.Vector2f
import org.slf4j.LoggerFactory

trait Direction {
  val dir: Vector2f
}

case class North() extends Direction {
  val dir = new Vector2f(0f, 1f)
}

case class East() extends Direction {
  val dir = new Vector2f(1f, 0f)
}

case class South() extends Direction {
  val dir = new Vector2f(0f, -1f)
}

case class West() extends Direction {
  val dir = new Vector2f(-1f, 0f)
}

trait CollisionResult

case class Collision(direction: Direction, intersectionAmount: Vector2f) extends CollisionResult

case class NoCollision() extends CollisionResult

object CollisionDetection {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val compass = Array[Direction](North(), East(), South(), West())

  def vectorDirection(v: Vector2f): Direction = {
    val n = v.normalize()
    compass.map(c => (c, new Vector2f(n).dot(c.dir))).maxBy(_._2)._1
  }

  /**
   * Collision detection using Axis Aligned Bounding Boxes.
   *
   * @param entityA An entity to check for collision
   * @param entityB An entity to check for collision
   * @return true if the two entities collide.
   */
  def checkCollision(entityA: Entity, entityB: Entity): Boolean = {
    val collisionX = entityA.position.x + entityA.scale.x >= entityB.position.x &&
      entityB.position.x + entityB.scale.x >= entityA.position.x
    val collisionY = entityA.position.y + entityA.scale.y >= entityB.position.y &&
      entityB.position.y + entityB.scale.y >= entityA.position.y
    val result     = collisionX && collisionY
    logger.info(s"checkCollision: $result")
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
    val center          = new Vector2f(ball.position).add(ball.radius, ball.radius)
    val aabbHalfExtents = new Vector2f(entity.scale.x / 2f, entity.scale.y / 2f)
    val aabbCenter      = new Vector2f(entity.position).add(aabbHalfExtents)
    val difference      = new Vector2f(center).sub(aabbCenter)
    val clamped         = new Vector2f(org.joml.Math.clamp(-aabbHalfExtents.x, aabbHalfExtents.x, difference.x),
                               org.joml.Math.clamp(-aabbHalfExtents.y, aabbHalfExtents.y, difference.y)
    )
    val closest         = new Vector2f(aabbCenter).add(clamped)
    val d2              = new Vector2f(closest).sub(center)

    if (d2.length() <= ball.radius) {
      logger.trace(s"checkCollision: ${vectorDirection(d2)}, diff: $d2")
      Collision(vectorDirection(d2), d2)
    } else {
      NoCollision()
    }
  }
}
