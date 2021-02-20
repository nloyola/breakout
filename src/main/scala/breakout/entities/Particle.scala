package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg.DenseVector

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

  override protected val _transform: Transform = Transform()

  private val sprite = Sprite(AssetPool.texture("assets/images/particle.png"))

  private val spriteRenderer = SpriteRenderer(entity = this, sprite = sprite)

  //private val rigidBody = RigidBody(this, 1, new Vector3f(), 0f)

  private val _velocity = DenseVector(0f, 0f, 0f)

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

  def color = spriteRenderer.color

  def color_=(c: DenseVector[Float]) = spriteRenderer.color := c

  def velocity_=(v: DenseVector[Float]) = velocity := v

  override def update(dt: Float): Unit = {
    super.update(dt)

    position -= _velocity.slice(0, 2) * dt

    if (_life > 0f) {
      lifeDecrease(dt * 3)
      spriteRenderer.color := DenseVector(spriteRenderer.color(0),
                                          spriteRenderer.color(1),
                                          spriteRenderer.color(2),
                                          spriteRenderer.color(3) - 8f * dt
      )
    }

    // if ((_life <= 0) && (startTime > 0)) {
    //   val lifeSpan = (System.nanoTime() - startTime) / 1000000
    //   logger.info(s"particle: id: $id, life span: ${lifeSpan} ms")
    // }

    ()
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
