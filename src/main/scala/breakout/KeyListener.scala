package breakout

import org.lwjgl.glfw.GLFW.{ GLFW_PRESS, GLFW_RELEASE }

import scala.collection.mutable.ArrayBuffer

object KeyListener {

  private val keyPressed = ArrayBuffer.fill(350)(false)

  def keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
    if (action == GLFW_PRESS) {
      keyPressed(key) = true
    } else if (action == GLFW_RELEASE) {
      keyPressed(key) = false
    }
  }

  def isKeyPressed(keyCode: Int): Boolean = {
    keyPressed(keyCode)
  }
}
