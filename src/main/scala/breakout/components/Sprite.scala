package breakout.components

import breakout.renderers.Texture
import breeze.linalg.DenseVector

final case class Sprite(
    width:     Int,
    height:    Int,
    texture:   Option[Texture],
    texCoords: Array[DenseVector[Float]]) {

  def texId(): Option[Int] = texture.map(_.id)

  override def toString: String = s"texture: $texture, texCoord: $texCoords"

}

object Sprite {

  def apply(texture: Texture): Sprite =
    Sprite(width = 0,
           height = 0,
           texture = Some(texture),
           texCoords =
             Array(DenseVector(1f, 1f), DenseVector(1f, 0f), DenseVector(0f, 0f), DenseVector(0f, 1f))
    )

  def apply(): Sprite =
    Sprite(width = 0,
           height = 0,
           texture = None,
           texCoords =
             Array(DenseVector(1f, 1f), DenseVector(1f, 0f), DenseVector(0f, 0f), DenseVector(0f, 1f))
    )

}
