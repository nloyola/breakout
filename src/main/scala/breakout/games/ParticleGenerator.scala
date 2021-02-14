package breakout.games

import breakout.components.RigidBody
import breakout.entities.{ BallParticle, Entity }
import breakout.scenes.Scene
import org.joml.{ Vector2f, Vector4f }
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class ParticleGenerator(scene: Scene) {

  private val maxParticles = 500

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val inactiveBallParticles = ArrayBuffer.empty[BallParticle]

  private val activeBallParticles = ArrayBuffer.empty[BallParticle]

  def init(zIndex: Int): Unit = {
    logger.debug("init")
    (0 until maxParticles).foreach { index =>
      val particle = BallParticle(0f, zIndex)
      inactiveBallParticles += particle
    }
  }

  def update(dt: Float, entity: Entity, numParticles: Int): Unit = {

    // handle dead particles
    val deadParticles = activeBallParticles.filter(_.isDead())
    activeBallParticles --= deadParticles
    inactiveBallParticles ++= deadParticles
    deadParticles.foreach(scene.removeEntity)

    // if (deadParticles.size > 0) {
    //   logger.info(s"dead particles: " + deadParticles.map(_.toString()).mkString(", "))
    // }

    // add new particles
    val newParticles = if (inactiveBallParticles.length >= numParticles) {
      val particles = inactiveBallParticles.take(numParticles)
      inactiveBallParticles --= particles
      activeBallParticles ++= particles
      particles
    } else {
      ArrayBuffer.empty[BallParticle]
    }

    newParticles.foreach { particle =>
      spawn(particle, entity, new Vector2f(entity.scale.x / 4f, entity.scale.y / 4f))
    }

    // if (newParticles.size > 0) {
    //   logger.info(s"new particles: " + newParticles.map(_.toString()).mkString(", "))
    // }

    // if (activeBallParticles.size > 0) {
    //   logger.info(s"active particles: " + activeBallParticles.map(_.id).mkString(", "))
    // }

    // if (inactiveBallParticles.size > 0) {
    //   logger.info(s"inactive particles: " + inactiveBallParticles.map(_.id).mkString(", "))
    // }
  }

  private def spawn(particle: BallParticle, entity: Entity, offset: Vector2f): Unit = {
    entity.component[RigidBody]().foreach { rb =>
      val rand   = scala.util.Random.nextFloat() * 5f
      val rColor = 0.5f + scala.util.Random.nextFloat()
      particle.position = new Vector2f(entity.position).add(rand, rand).add(offset)
      particle.scale(entity.scale.x / 2.5f, entity.scale.y / 2.5f)
      particle.life = 1f
      particle.velocity = new Vector2f(rb.velocity.x, rb.velocity.y).mul(0.1f)
      particle.setColor(new Vector4f(rColor, rColor, rColor, 1f))
      scene.addEntityToScene(particle)
    }
  }
}
