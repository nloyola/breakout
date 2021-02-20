package breakout

//import org.slf4j.LoggerFactory
import breeze.linalg._

case class Camera(position: DenseVector[Float], width: Float, height: Float) {
  //private val logger = LoggerFactory.getLogger(this.getClass)

  private val left   = 0f
  private val right  = width
  private val bottom = height
  private val top    = 0f
  private val zNear  = 0f
  private val zFar   = 100f

  val projectionMatrix =
    DenseMatrix((2f / (right - left), 0f, 0f, -(right + left) / (right - left)),
                (0f, 2 / (top - bottom), 0f, -(top + bottom) / (top - bottom)),
                (0f, 0f, -2f / (zFar - zNear), -(zFar + zNear) / (zFar - zNear)),
                (0f, 0f, 0f, 1f)
    )

  // From https://www.3dgep.com/understanding-the-view-matrix/#The_View_Matrix
  def getViewMatrix(): DenseMatrix[Float] = {
    // val cameraFront = new Vector3f(0.0f, 0.0f, -1.0f)
    // val cameraUp    = new Vector3f(0.0f, 1.0f, 0.0f)
    // val viewMatrix  = new Matrix4f
    // viewMatrix.identity
    // viewMatrix.lookAt(new Vector3f(position(0), position(1), 20.0f),
    //                   cameraFront.add(position(0), position(1), 0.0f),
    //                   cameraUp
    // )
    val eye    = DenseVector(position(0), position(1), 20f)
    val target = DenseVector(0.0f, 0.0f, -1.0f) + DenseVector(position(0), position(1), 0f)
    val up     = DenseVector(0f, 1f, 0f)
    val zAxis  = normalize(eye - target)
    val xAxis  = normalize(cross(up, zAxis))
    val yAxis  = cross(zAxis, xAxis)

    // Create a 4x4 orientation matrix from the right, up, and forward vectors. This is transposed which is
    // equivalent to performing an inverse if the matrix is orthonormalized (in this case, it is).
    val orientation = DenseMatrix((xAxis(0), yAxis(0), zAxis(0), 0f),
                                  (xAxis(1), yAxis(1), zAxis(1), 0f),
                                  (xAxis(2), yAxis(2), zAxis(2), 0f),
                                  (0f, 0f, 0f, 1f)
    )

    // Create a 4x4 translation matrix. The eye position is negated which is equivalent to the inverse of the
    // translation matrix. T(v)^-1 == T(-v)
    val translation =
      DenseMatrix((1f, 0f, 0f, 0f), (0f, 1f, 0f, 0f), (0f, 0f, 1f, 0f), (-eye(0), -eye(1), -eye(2), 1f))

    // Combine the orientation and translation to compute the final view matrix. Note that the order of
    // multiplication is reversed because the matrices are already inverted.
    orientation * translation
  }

  def withPosition(x: Float, y: Float) = copy(position = DenseVector(x, y))

}
