package breakout.games

import breakout.entities.{ Background, Entity, Paddle }
import scala.collection.mutable.ArrayBuffer

trait GameState

case class GameMenu() extends GameState

case class GameActive() extends GameState

case class GameWin() extends GameState

class BreakoutGame(width: Float, height: Float) {

  // private var state = GameMenu()

  private val _entities = ArrayBuffer.empty[Entity]

  private val levels = ArrayBuffer.empty[GameLevel]

  //private var level = 0

  def entities = _entities

  def update(dt: Float): Unit = {}

  private def init(): Unit = {

    val levelOne = new GameLevel()
    levelOne.load(GameLevel.levelOne, width, height / 2f)

    levels += levelOne

    _entities += Paddle(width, height, 1)
    _entities += Background(width, height, -10)
    _entities ++= levelOne.blocks
  }

  init()

}
