package breakout

import org.joml.{ Matrix4f, Vector2f, Vector3f };

case class Camera(position: Vector2f, width: Float, height: Float) {

  def projectionMatrix: Matrix4f = {
    val projectionMatrix = new Matrix4f
    projectionMatrix.identity
    projectionMatrix.ortho(0.0f, width, height, 0.0f, 0.0f, 100.0f)
  }

  def getViewMatrix(): Matrix4f = {
    val cameraFront = new Vector3f(0.0f, 0.0f, -1.0f)
    val cameraUp    = new Vector3f(0.0f, 1.0f, 0.0f)
    val viewMatrix  = new Matrix4f
    viewMatrix.identity
    viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                      cameraFront.add(position.x, position.y, 0.0f),
                      cameraUp
    )
  }

  def withPosition(x: Float, y: Float) = copy(position = new Vector2f(x, y))

}
