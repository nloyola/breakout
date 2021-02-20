package breakout.renderers

import breakout.Window
import breakout.components.SpriteRenderer
import breakout.entities.Entity
import breakout.util.AssetPool
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl.{ GL15, GL20C }
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class RenderBatch(private val maxBatchSize: Int, private val zIndex: Int) extends Ordered[RenderBatch] {

  private val logger = LoggerFactory.getLogger(this.getClass)

  // Vertex
  // ======
  // Pos               Color                         tex coords     tex id
  // float, float,     float, float, float, float    float, float   float
  private val FLOAT_BYTES     = 4
  private val POS_SIZE        = 2
  private val COLOR_SIZE      = 4
  private val TEX_COORDS_SIZE = 2
  private val TEX_ID_SIZE     = 1

  private val POS_OFFSET        = 0L
  private val COLOR_OFFSET      = POS_OFFSET + POS_SIZE * FLOAT_BYTES
  private val TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * FLOAT_BYTES
  private val TEX_ID_OFFSET     = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * FLOAT_BYTES

  private val VERTEX_SIZE       = 9
  private val VERTEX_SIZE_BYTES = VERTEX_SIZE * FLOAT_BYTES

  private val shader   = AssetPool.shader("assets/shaders/default.glsl")
  private val sprites  = new ArrayBuffer[SpriteRenderer](maxBatchSize)
  private val vertices = new Array[Float](maxBatchSize * 4 * VERTEX_SIZE)
  private val texSlots = Array[Int](0, 1, 2, 3, 4, 5, 6, 7)

  private val textures = ArrayBuffer.empty[Texture]
  private var vaoID    = 0
  private var vboID    = 0
  private var _hasRoom = true

  def hasRoom(): Boolean = _hasRoom

  def hasTextureRoom(): Boolean = textures.size < 8

  def hasTexture(tex: Texture): Boolean = textures.contains(tex)

  def getZIndex(): Int = zIndex

  def compare(that: RenderBatch) = this.zIndex - that.zIndex

  def start(): Unit = {
    logger.trace(s"start")
    vaoID = glGenVertexArrays
    glBindVertexArray(vaoID)

    // Allocate space for vertices
    vboID = GL15.glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboID)
    glBufferData(GL_ARRAY_BUFFER, vertices.size.toLong * FLOAT_BYTES, GL_DYNAMIC_DRAW)

    // Create and upload indices buffer
    val eboID   = glGenBuffers()
    val indices = generateIndices()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

    // Enable the buffer attribute pointers
    GL20C.glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET)
    glEnableVertexAttribArray(0)

    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET)
    glEnableVertexAttribArray(1)

    glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET)
    glEnableVertexAttribArray(2)

    glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET)
    glEnableVertexAttribArray(3)
  }

  def addSprite(spr: SpriteRenderer): Unit = {
    logger.trace(s"addSprite: spr: $spr")

    // Get index and add renderObject
    sprites += spr

    spr.texture.foreach { t =>
      if (!textures.contains(t)) {
        //logger.debug(s"addSprite: added texture $t")
        textures += t
      }
    }

    // Add properties to local vertices array
    loadVertexProperties(spr)

    _hasRoom = sprites.size < maxBatchSize
  }

  def destroyEntity(entity: Entity): Unit = {
    entity.component[SpriteRenderer]().foreach { sr =>
      if (sprites.contains(sr)) {

        val index = sprites.indexOf(sr)
        sprites.slice(index + 1, sprites.length).foreach(_.setDirty())
        sprites -= sr
        logger.trace(s"destroyEntity: $entity, position: ${entity.position}, index: $index")
      }
    }
    ()
  }

  def render(): Unit = {
    var rebufferData = false

    //logger.debug(s"render: numSprites: $numSprites")
    sprites.foreach { sprite =>
      if (sprite.isDirty) {
        loadVertexProperties(sprite)
        sprite.setClean()
        rebufferData = true
      }
    }

    if (rebufferData) {
      glBindBuffer(GL_ARRAY_BUFFER, vboID)
      logger.trace(s"vertices: " + vertices.take(40).map(_.toString).mkString(", "))
      glBufferSubData(GL_ARRAY_BUFFER, 0, vertices)
    }

    shader.use()
    shader.uploadDenseMatrix("uProjection", Window.scene.camera.projectionMatrix)
    shader.uploadDenseMatrix("uView", Window.scene.camera.getViewMatrix())

    textures.zipWithIndex.foreach { case (t, i) =>
      logger.trace(s"render: index: $i, texture: $t")
      glActiveTexture(GL_TEXTURE0 + i + 1)
      t.bind()
    }
    shader.uploadIntArray("uTextures", texSlots)

    glBindVertexArray(vaoID)
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)

    logger.trace(s"rendering ${sprites.size} sprites")
    glDrawElements(GL_TRIANGLES, sprites.size * 6, GL_UNSIGNED_INT, 0)

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glBindVertexArray(0)

    textures.foreach(_.unbind())
    shader.detach()

    //logger.debug("render: done")
  }

  private def generateIndices(): Array[Int] = {
    // 6 indices per quad (3 per triangle)
    val elements = new Array[Int](6 * maxBatchSize)

    (0 until maxBatchSize).foreach { index =>
      val offsetArrayIndex = 6 * index;
      val offset           = 4 * index;

      // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
      // Triangle 1
      elements(offsetArrayIndex) = offset + 3;
      elements(offsetArrayIndex + 1) = offset + 2;
      elements(offsetArrayIndex + 2) = offset + 0;

      // Triangle 2
      elements(offsetArrayIndex + 3) = offset + 0;
      elements(offsetArrayIndex + 4) = offset + 2;
      elements(offsetArrayIndex + 5) = offset + 1;
    }
    elements
  }

  private def loadVertexProperties(sprite: SpriteRenderer): Unit = {
    val index = sprites.indexOf(sprite)

    // Find offset within array (4 vertices per sprite)
    var offset = index * 4 * VERTEX_SIZE

    val color     = sprite.color
    val texCoords = sprite.texCoords
    var texId     = 0;

    sprite.texture.foreach { tex =>
      texId = textures.indexOf(tex) + 1
    }

    val entity    = sprite.entity
    val transform = entity.transform

    // entity match {
    //   case e: breakout.entities.Ball =>
    //     logger.info(s"ball position: ${entity.transform}")
    //   case _ =>
    // }

    logger.trace(s"entity name: ${entity.name}, entity pos: ${entity.position}, index: $index")

    // Add vertices with the appropriate properties
    Array((0, 1f, 1f), (1, 1f, 0f), (2, 0f, 0f), (3, 0f, 1f)).foreach { case (v, xAdd, yAdd) =>
      // Load position
      vertices(offset) = transform.position(0) + (xAdd * transform.scale(0))
      vertices(offset + 1) = transform.position(1) + (yAdd * transform.scale(1))

      logger.trace(s"${entity.name}, $index, $v, ${vertices(offset)}, ${vertices(offset + 1)}")

      // Load color
      vertices(offset + 2) = color(0)
      vertices(offset + 3) = color(1)
      vertices(offset + 4) = color(2)
      vertices(offset + 5) = color(3)

      // Load texture coordinates
      vertices(offset + 6) = texCoords(v)(0)
      vertices(offset + 7) = texCoords(v)(1)

      // Load texture id
      vertices(offset + 8) = texId.toFloat

      offset += VERTEX_SIZE
    }

    ()
  }

}
