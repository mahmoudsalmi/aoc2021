import math.BigInt.int2bigInt
import scala.annotation.tailrec

object day21 extends App {
  object day21Tools {
    def gcd(a: Int, b: Int): Int = BigInt(a).gcd(BigInt(b)).toInt

    def getPerfectCycleSize(valuesSize: Int, stepSize: Int): Int = stepSize / gcd(stepSize, valuesSize % stepSize)

    def buildCycle(diceFaces: Int, turnSize: Int, max: Int): List[Int] =
      Range.Int.inclusive(1, getPerfectCycleSize(diceFaces, turnSize), 1)
        .flatMap(_ => Range.Int.inclusive(1, diceFaces, 1))
        .grouped(turnSize)
        .map[Int](_.sum)
        .map(_ % max)
        .toList

    def getValueFromCycle(cycle: List[Int], idx: Int): Int = cycle(idx % cycle.length)

    def defaultScore(index: Int): Int = 0

    def combine(la: List[List[Int]], lb: List[List[Int]]): List[List[Int]] = (for a <- la; b <- lb yield (a, b)).map(_ ::: _)

    def generateArrangement(size: Int, sourceSize: Int): List[List[Int]] = (1 to size).toList
      .map(_ => Range.Int.inclusive(1, sourceSize, 1).toList.map(e => List(e)))
      .reduceRight((la, lb) => (for a <- la; b <- lb yield (a, b)).map(_ ::: _))
  }

  import day21Tools._

  case class Game(diceFaces: Int, turnSize: Int, max: Int, target: Int, players: Int) {
    private val cycle: List[Int] = buildCycle(diceFaces, turnSize, max)

    def stepValue(step: Int): Int = getValueFromCycle(cycle, step)

    def absoluteStep(player: Int, turn: Int): Int = player + players * turn

    def reducePosition(position: Int): Int = if position <= max then position else reducePosition(position - max)

    def move(from: Int, distance: Int): Int = reducePosition(from + distance)

    def movePlayer(player: Int, from: Int, turn: Int): Int = move(from, stepValue(absoluteStep(player, turn)))

    def move(from: List[Int], distance: List[Int]): List[Int] = from.zipWithIndex.map { case (f, i) => move(f, distance(i)) }

    def generateAllNextValues(): Map[Int, Int] =
      generateArrangement(turnSize, diceFaces)
        .map(l => l.sum)
        .groupBy(identity)
        .view.mapValues(_.size)
        .toMap

    def nextPlayer(currentPlayer: Int): Int = (currentPlayer + 1) % players
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

    def toGameResult: GameResult = GameResult(scores.map(_.toPlayerResult))

    def update(player: Int, playerScore: PlayerScore): GameScore = GameScore(game, scores.zipWithIndex.map { case (ps, i) => if i == player then playerScore else ps })
  }

  object GameScore {
    def initScore(game: Game, opts: Option[GameScore]): GameScore = opts match {
      case Some(s) => s
      case None => GameScore(game, List.fill(game.players)(PlayerScore(game, 0, 0)))
    }
  }

  case class PlayerResult(winner: Boolean, score: Int, playedTurn: Int, diceRolls: Int)

  case class GameResult(players: List[PlayerResult]) {
    val diceRolls: Int = players.map(_.diceRolls).sum
  }

  @tailrec
  def play(game: Game, positions: List[Int], score: Option[GameScore] = None, turn: Int = -1, player: Int = 0): GameResult = {
    val currentScore = GameScore.initScore(game, score)
    val currentTurn = if player == 0 then turn + 1 else turn

    val nextPositions = positions.zipWithIndex.map { case (pos, i) => if i == player then game.movePlayer(player, positions(player), currentTurn) else pos }
    val nextScore = currentScore.update(player, PlayerScore(game, currentScore.score(player) + nextPositions(player), currentTurn + 1))

    if (nextScore.endGame >= 0) {
      return nextScore.toGameResult
    }

    play(game, nextPositions, Some(nextScore), currentTurn, game.nextPlayer(player))
  }

  def play2(game: Game, positions: List[Int], score: Option[GameScore] = None, turn: Int = -1, player: Int = 0): List[Long] = {
    val currentScore = GameScore.initScore(game, score)
    val currentTurn = if player == 0 then turn + 1 else turn

    val endGame = currentScore.endGame
    if (endGame >= 0) {
      return currentScore.scores.map(p => if p.winner then 1L else 0L)
    }

    val res = game.generateAllNextValues()
      .map {
        case (distance, count) =>
          val nextPositions = positions.zipWithIndex.map { case (pos, i) => if i == player then game.move(positions(player), distance) else pos }
          val nextScore = currentScore.update(player, PlayerScore(game, currentScore.score(player) + nextPositions(player), currentTurn + 1))
          (nextPositions, nextScore, count)
      }
      .map {
        case (positions, nextScore, count) => play2(game, positions, Some(nextScore), currentTurn, game.nextPlayer(player)).map(_ * count)
      }
      .reduce((a, b) => a.zip(b).map { case (i, j) => i + j })

    res
  }

  def part1(data: List[Int]): Int = {
    val game = Game(100, 3, 10, 1000, 2)
    val result = play(game, data)
    result.players.filter(!_.winner).head.score * result.diceRolls
  }

  def part2(data: List[Int]): Long = {
    val game = Game(3, 3, 10, 21, 2)
    play2(game, data).max
  }

  println("----(AOC2021 - Day 21)-----------------------[Scala]----")

  val exemple = List(4, 8)
  println("Exemple :: Part 1 ====>     " + part1(exemple))
  println("Exemple :: Part 2 ====>     " + part2(exemple))
  println("--------------------------------------------------------")

  val input = List(10, 6)
  println("Input   :: Part 1 ====>     " + part1(input))
  println("Input   :: Part 2 ====>     " + part2(input))
  println("--------------------------------------------------------")
}
