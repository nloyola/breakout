package breakout.renderers

import org.joml._
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.{ glGetShaderInfoLog, _ }
import org.slf4j.LoggerFactory

import scala.collection.mutable.Map
import scala.io.Source

class Shader(val filepath: String) {

  val sections        = Map.empty[String, List[String]]
  var shaderProgramID = 0
  var beingUsed       = false

  private val logger = LoggerFactory.getLogger(this.getClass)

  /**
   * Compile and link shaders
   */
  def compile(): Unit = {
    // First load and compile the vertex shader
    val vertexShaderId = sections.get("vertex").map { source =>
      val vertexID = glCreateShader(GL_VERTEX_SHADER)

      logger.debug(s"vertex shader source: {}", source.mkString("\n"))

      // Pass the shader source to the GPU
      glShaderSource(vertexID, source.mkString("\n"))
      glCompileShader(vertexID)

      // Check for errors in compilation
      val success = glGetShaderi(vertexID, GL_COMPILE_STATUS)
      if (success == GL_FALSE) {
        val len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH)
        throw new Error(
          filepath + "'\n\tvertex shader compilation failed.\n" +
            glGetShaderInfoLog(vertexID, len)
        )
      }
      vertexID
    }

    val fragmentShaderId = sections.get("fragment").map { source =>
      // First load and compile the fragment shader
      val fragmentID = glCreateShader(GL_FRAGMENT_SHADER)
      // Pass the shader source to the GPU
      glShaderSource(fragmentID, source.mkString("\n"))
      glCompileShader(fragmentID)

      // Check for errors in compilation
      val success = glGetShaderi(fragmentID, GL_COMPILE_STATUS)
      if (success == GL_FALSE) {
        val len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH)
        throw new Error(
          filepath + "'\n\tFragment shader compilation failed.\n" +
            glGetShaderInfoLog(fragmentID, len)
        )
      }
      fragmentID
    }

    // Link shaders and check for errors
    shaderProgramID = glCreateProgram()
    vertexShaderId.foreach(s => glAttachShader(shaderProgramID, s))
    fragmentShaderId.foreach(s => glAttachShader(shaderProgramID, s))
    glLinkProgram(shaderProgramID)

    // Check for linking errors
    val success = glGetProgrami(shaderProgramID, GL_LINK_STATUS)
    if (success == GL_FALSE) {
      val len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH)
      throw new Error(
        filepath + "'\n\tLinking of shaders failed.\n" +
          glGetProgramInfoLog(shaderProgramID, len)
      )
    }
  }

  def use(): Unit = {
    if (shaderProgramID == 0) {
      throw new Error("shader was never compiled")
    }

    if (!beingUsed) {
      logger.debug(s"programId: $shaderProgramID")
      glUseProgram(shaderProgramID)
      beingUsed = true
    }
  }

  def detach(): Unit = {
    logger.debug(s"programId: $shaderProgramID")
    glUseProgram(0)
    beingUsed = false
  }

  def uploadMat4f(varName: String, mat4: Matrix4f): Unit = {
    val varLocation = glGetUniformLocation(shaderProgramID, varName)
    use()
    val matBuffer   = BufferUtils.createFloatBuffer(16)
    mat4.get(matBuffer)
    glUniformMatrix4fv(varLocation, false, matBuffer)
  }

  def uploadIntArray(varName: String, array: Array[Int]): Unit = {
    logger.debug(s"uploadIntArray: array: ${array.length}")
    val varLocation = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform1iv(varLocation, array)
  }

  private def init(): Unit = {
    val contents = Source.fromFile(filepath).getLines().toList.mkString("\n")
    sections ++= contents.split("#type\\s+").filter(_.trim != "").map { section =>
      val lines = section.split("\n").toList
      (lines.head -> lines.tail)
    }
  }

  init()
}
