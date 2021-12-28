import day20.{Data, nextImage, parseData}

import math.BigInt.int2bigInt
import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object day21 extends App {
  object day21Tools {
    def gcd(a: Int, b: Int): Int = BigInt(a).gcd(BigInt(b)).toInt

    def getPerfectCycleSize(valuesSize: Int, stepSize: Int): Int = stepSize / gcd(stepSize, valuesSize % stepSize)

    def buildCycle(diceFaces: Int, turnSize: Int, max: Int): List[Int] =
      (1 to getPerfectCycleSize(diceFaces, turnSize))
        .flatMap(_ => 1 to diceFaces)
        .grouped(turnSize)
        .map(_.sum.toInt)
        .map(_ % max)
        .toList

    def getValueFromCycle(cycle: List[Int], idx: Int): Int = cycle(idx % cycle.length)

    def defaultScore(index: Int): Int = 0
  }
  import day21Tools._

  case class Game(diceFaces: Int, turnSize: Int, max: Int, target: Int, players: Int) {
    private val cycle: List[Int] = buildCycle(diceFaces, turnSize, max)

    def stepValue(step: Int): Int = getValueFromCycle(cycle, step)

    def absoluteStep(player: Int, turn: Int): Int = player + players * turn

    def reducePosition(position: Int): Int = if position <= max then position else reducePosition(position - max)

    def move(from: Int, distance: Int): Int = reducePosition(from + distance)

    def movePlayer(player: Int, from: Int, turn: Int): Int = move(from, stepValue(absoluteStep(player, turn)))
  }

  case class PlayerScore(game: Game, score: Int, playedTurn: Int) {
    def diceRolls: Int = playedTurn * game.turnSize

    def winner: Boolean = score >= game.target

    def toPlayerResult: PlayerResult = PlayerResult(winner, score, playedTurn, diceRolls)
  }

  case class GameScore(game: Game, scores: List[PlayerScore] = List[PlayerScore]()) {
    def player(id: Int): PlayerScore = scores.applyOrElse(id, _ => PlayerScore(game, 0, 0))

    def score(id: Int): Int = player(id).score

    def endGame: Int = if scores.isEmpty then -1 else scores.indexWhere(_.winner)

    def toGameResult: GameResult = GameResult( scores.map(_.toPlayerResult) )
  }

  case class PlayerResult(winner: Boolean, score: Int, playedTurn: Int, diceRolls: Int)

  case class GameResult(players: List[PlayerResult]) {
    val diceRolls: Int = players.map(_.diceRolls).sum
  }

  @tailrec
  def play(game: Game, positions: List[Int], score: Option[GameScore] = None, turn: Int = 0): GameResult = {

    val currentScore: GameScore = score match {
      case Some(s) => s
      case None => GameScore(game)
    }

    val nextPositions = positions.zipWithIndex.map {
      case (position, player) => game.movePlayer(player, position, turn)
    }

    val nextScore = GameScore(
      game,
      nextPositions.zipWithIndex
        .map { case (position, player) => position + currentScore.score(player) }
        .map(score => PlayerScore(game, score, turn + 1))
    )

    val endGame = nextScore.endGame
    if (nextScore.endGame >= 0) {
      return GameScore(
        game, List.concat(
          nextScore.scores.slice(0, endGame + 1),
          currentScore.scores.slice(endGame + 1, game.players)
        )
      ).toGameResult
    }

    play(game, nextPositions, Some(nextScore), turn + 1)
  }

  def play2()

  def part1(data: List[Int]): Int = {
    val game = Game(100, 3, 10, 1000, 2)
    val result = play(game, data)
    println(result)
    result.players.filter(!_.winner).head.score * result.diceRolls
  }

  def part2(data: List[Int]): Int = -1

  println("----(AOC2021 - Day 21)-----------------------[Scala]----")

  println("Exemple :: Part 1 ====>     " + part1(exemple))
  println("Exemple :: Part 2 ====>     " + part2(exemple))
  println("--------------------------------------------------------")

  println("Input   :: Part 1 ====>     " + part1(input))
  println("Input   :: Part 2 ====>     " + part2(input))
  println("--------------------------------------------------------")
}
