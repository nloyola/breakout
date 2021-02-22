package breakout.entities

import breakout.Transform
import breakout.components.{ Sprite, SpriteRenderer }
import breakout.util.AssetPool
import breeze.linalg.DenseVector

trait Particle extends Entity {

  protected var life: Float

  def makeAlive() = life = 1f

  def lifeDecrease(amount: Float) = life = life - amount

  def isAlive(): Boolean = life > 0f

  def isDead(): Boolean = !isAlive()

}

case class BallParticle(zIndex: Int) extends Particle {

  //private val logger = LoggerFactory.getLogger(this.getClass)

  protected var life = 0f

  override val name: String = "BallParticle"

  override val transform: Transform = Transform()

  private val sprite = Sprite(AssetPool.texture("assets/images/particle.png"))

  private val spriteRenderer = SpriteRenderer(entity = this, sprite = sprite)

  //private val rigidBody = RigidBody(this, 1, new Vector3f(), 0f)

  private val _velocity = DenseVector(0f, 0f, 0f)

  val id = BallParticle.id

  // private var startTime = 0L

  def velocity = _velocity

  def color = spriteRenderer.color

  def color_=(c: DenseVector[Float]) = spriteRenderer.color := c

  def velocity_=(v: DenseVector[Float]) = velocity := v

  override def update(dt: Float): Unit = {
    super.update(dt)

    position -= _velocity.slice(0, 2) * dt

    if (isAlive()) {
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
