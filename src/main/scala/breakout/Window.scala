package breakout

import breakout.scenes.{ BreakoutScene, Scene }
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryUtil._

object Window {

  private val title = "Breakout"

  private var _width = 1920

  private var _height = 1080

  private var currentScene: Option[Scene] = None

  // The window handle
  private lazy val window = glfwCreateWindow(_width, _height, title, NULL, NULL)

  def scene: Scene = {
    currentScene match {
      case Some(s) => s
      case _       => throw new Error(s"Scene not assigned")
    }
  }

  def scene_=(newScene: Int) = {
    newScene match {
      case 0 => currentScene = Some(new BreakoutScene)
      // case 1 => currentScene = Some(new LevelEditorScene)
      case _ => throw new Error(s"Unknown scene: $newScene")
    }

    currentScene.foreach { s =>
      s.load()
      s.init()
      s.start()
    }
  }

  def updateScene(dt: Float): Unit = {
    currentScene.foreach { s =>
      s.update(dt)
    }

  }

  def width: Int = _width

  // def width_=(w: Int) = _width = w

  def getHeight: Int = _height

  // def height_=(h: Int) = _height = h

  private def init(): Unit = {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }

    // Configure GLFW
    glfwDefaultWindowHints // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window

    if (window == NULL) {
      throw new IllegalStateException("Failed to create the GLFW window.")
    }

    // // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    // glfwSetKeyCallback(
    //   window,
    //   (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
    //     if ((key == GLFW_KEY_ESCAPE) && (action == GLFW_RELEASE)) {
    //       glfwSetWindowShouldClose(window, true)
    //     }
    //   })

    def keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = {
      // temporary - remove after game is implemented
      if ((key == GLFW_KEY_ESCAPE) && (action == GLFW_RELEASE)) {
        glfwSetWindowShouldClose(window, true)
      }

      KeyListener.keyCallback(window, key, scancode, action, mods)
    }

    glfwSetCursorPosCallback(window, MouseListener.mousePosCallback)
    glfwSetMouseButtonCallback(window, MouseListener.mouseButtonCallback)
    glfwSetScrollCallback(window, MouseListener.mouseScrollCallback)
    glfwSetKeyCallback(window, keyCallback)

    def windowSizeCallback(w: Long, newWidth: Int, newHeight: Int) = {
      _width = newWidth
      _height = newHeight
    }

    glfwSetWindowSizeCallback(window, windowSizeCallback)

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities

    glEnable(GL_BLEND)
    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)

    scene = 0
  }

  private def loop(): Unit = {
    val r = 1.0f
    val b = 1.0f
    val g = 1.0f
    val a = 1.0f

    var beginTime = glfwGetTime.toFloat
    var endTime   = 0.0f
    var dt        = -1.0f

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents

      glClearColor(r, g, b, a)
      //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer
      glClear(GL_COLOR_BUFFER_BIT)

      if (dt >= 0) {
        updateScene(dt)
      }

      glfwSwapBuffers(window) // swap the color buffers

      endTime = glfwGetTime.toFloat
      dt = endTime - beginTime
      beginTime = endTime
    }
  }

  def run(): Unit = {
    init()
    loop()

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    // Terminate GLFW and free the error callback
    glfwTerminate
    glfwSetErrorCallback(null).free
  }

}
