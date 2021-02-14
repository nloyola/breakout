package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import breakout.util.AssetPool
import org.joml.{ Vector2f, Vector4f }
//import org.slf4j.LoggerFactory

trait Particle extends Entity {

  protected var _life: Float

  def life = _life

  def life_=(l: Float) = _life = l

  def lifeDecrease(amount: Float) = _life = _life - amount

  def isDead(): Boolean = _life <= 0f

}

case class BallParticle(protected var _life: Float, protected val _zIndex: Int) extends Particle {

  //private val logger = LoggerFactory.getLogger(this.getClass)

  override val name: String = "BallParticle"

  override protected val _transform: Transform = Transform(new Vector2f(), new Vector2f())

  private val sprite = Sprite(AssetPool.texture("assets/images/particle.png"))

  private val spriteRenderer = SpriteRenderer(entity = this, sprite = sprite)

  //private val rigidBody = RigidBody(this, 1, new Vector3f(), 0f)

  private val _velocity = new Vector2f()

  val id = BallParticle.id

  // private var startTime = 0L

  override def life_=(l: Float) = {
    super.life = l

    // if (_life >= 1f) {
    //   startTime = System.nanoTime()
    //   logger.info(s"particle started: id: $id, life: $l")
    // }
  }

  def velocity = _velocity

  def setColor(c: Vector4f): Unit = spriteRenderer.setColor(c)

  def velocity_=(v: Vector2f) = {
    velocity.x = v.x
    velocity.y = v.y
  }

  override def update(dt: Float): Unit = {
    super.update(dt)

    position = new Vector2f(position).sub(new Vector2f(_velocity).mul(dt))

    if (_life > 0f) {
      lifeDecrease(dt * 3)
      spriteRenderer.setColor(spriteRenderer.color.x,
                              spriteRenderer.color.y,
                              spriteRenderer.color.z,
                              spriteRenderer.color.w - 8f * dt
      )
    }

    // if ((_life <= 0) && (startTime > 0)) {
    //   val lifeSpan = (System.nanoTime() - startTime) / 1000000
    //   logger.info(s"particle: id: $id, life span: ${lifeSpan} ms")
    // }
  }

  override def toString: String = s"particle: id: $id"

  addComponent(spriteRenderer)
}

object BallParticle {
  var _id = 0L

  def id: Long = {
    val result = _id
    _id = _id + 1L
    result
  }
}
