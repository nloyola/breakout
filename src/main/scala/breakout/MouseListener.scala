package breakout

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import scala.collection.mutable.ArrayBuffer

object MouseListener {
  private var _scrollX = 0.0
  private var _scrollY = 0.0
  private var xPos     = 0.0
  private var yPos     = 0.0
  private var lastY    = 0.0
  private var lastX    = 0.0

  private val mouseButtonPressed = ArrayBuffer(false, false, false)
  private var isMouseDragging    = false

  def mousePosCallback(window: Long, xpos: Double, ypos: Double): Unit = {
    lastX = xPos
    lastY = yPos
    this.xPos = xpos
    this.yPos = ypos
    isMouseDragging = mouseButtonPressed(0) || mouseButtonPressed(1) || mouseButtonPressed(2)
  }

  def mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int): Unit = {
    if (action == GLFW_PRESS) {
      if (button < mouseButtonPressed.size) {
        mouseButtonPressed(button) = true
      }
    } else if (action == GLFW_RELEASE) {
      if (button < mouseButtonPressed.size) {
        mouseButtonPressed(button) = false
        isMouseDragging = false
      }
    }
  }

  def mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double): Unit = {
    _scrollX = xOffset
    _scrollY = yOffset
  }

  def endFrame(): Unit = {
    _scrollX = 0
    _scrollY = 0
    lastX = xPos
    lastY = yPos
  }

  def x: Float = xPos.toFloat

  def y: Float = yPos.toFloat

  def dx: Float = (lastX - xPos).toFloat

  def dy: Float = (lastY - yPos).toFloat

  def scrollX: Float = _scrollX.toFloat

  def scrollY: Float = _scrollY.toFloat

  def isDragging(): Boolean = isMouseDragging

  def mouseButtonDown(button: Int): Boolean = {
    if (button < mouseButtonPressed.length) mouseButtonPressed(button)
    else return false
  }
}
