import scala.collection.immutable.Range.Exclusive
import scala.collection.immutable.SortedSet
import scala.io.Source

object day22 extends App {

  object containers {
    type StateGrid = Array[Array[Array[Boolean]]]

    case class Action(actionType: Boolean, xMin: Int, xMax: Int, yMin: Int, yMax: Int, zMin: Int, zMax: Int) {

      def outRange(limit: Int): Boolean = xMax < -limit || xMin > limit || yMax < -limit || yMin > limit || zMax < -limit || zMin > limit

      def reduce(limit: Int): Action = Action(actionType, Integer.max(-limit, xMin), Integer.min(limit, xMax), Integer.max(-limit, yMin), Integer.min(limit, yMax), Integer.max(-limit, zMin), Integer.min(limit, zMax))
    }

    case class Coordinates(x: IndexedSeq[Int], y: IndexedSeq[Int], z: IndexedSeq[Int]) {
      def initGrid: StateGrid = Array.fill(x.size)(
        Array.fill(y.size)(
          Array.fill(z.size)(false)
        )
      )
    }

    case class CompressedCoordinates(x: SortedSet[Int] = SortedSet[Int](), y: SortedSet[Int] = SortedSet[Int](), z: SortedSet[Int] = SortedSet[Int]()) {

      def put(action: Action): CompressedCoordinates = CompressedCoordinates(
        x.concat(Seq[Int](action.xMin, action.xMax + 1)),
        y.concat(Seq[Int](action.yMin, action.yMax + 1)),
        z.concat(Seq[Int](action.zMin, action.zMax + 1))
      )

      def toCoord: Coordinates = Coordinates(x.toIndexedSeq, y.toIndexedSeq, z.toIndexedSeq)
    }

    case class Data(private val inputActions: List[Action], max: Int = Int.MaxValue) {
      val actions: List[Action] = inputActions.filter(a => !a.outRange(max)).map(_.reduce(max))
      val compressedCoordinates: CompressedCoordinates = actions.foldRight(CompressedCoordinates())((a, coord) => coord.put(a))
    }

    class State(val coord: Coordinates) {

      val value: StateGrid = coord.initGrid

      def xIdx(v: Int): Int = coord.x.indexOf(v)

      def yIdx(v: Int): Int = coord.y.indexOf(v)

      def zIdx(v: Int): Int = coord.z.indexOf(v)

      def applyAction(action: Action): Unit =
        (xIdx(action.xMin) until xIdx(action.xMax + 1)).foreach { x =>
          (yIdx(action.yMin) until yIdx(action.yMax + 1)).foreach { y =>
            (zIdx(action.zMin) until zIdx(action.zMax + 1)).foreach { z =>
              value(x)(y)(z) = action.actionType
            }
          }
        }

      def countOn: Long =
        coord.x.indices.foldLeft(0L) {
          case (xSum, x) => xSum + coord.y.indices.foldLeft(0L) {
            case (ySum, y) => ySum + coord.z.indices.map { z =>
              if (value(x)(y)(z)) {
                (coord.x(x + 1) - coord.x(x)).toLong
                  * (coord.y(y + 1) - coord.y(y)).toLong
                  * (coord.z(z + 1) - coord.z(z)).toLong
              } else {
                0
              }
            }.sum
          }
        }
    }
  }

  export containers._

  def parseLine(line: String): Action = line match {
    case s"${actionType} x=${xMin}..${xMax},y=${yMin}..${yMax},z=${zMin}..${zMax}" =>
      Action(actionType == "on", xMin.toInt, xMax.toInt, yMin.toInt, yMax.toInt, zMin.toInt, zMax.toInt)
  }

  def parseData(filename: String): List[Action] = {
    val source = Source.fromFile(filename)

    val actions = source.getLines().map(parseLine).toList
    source.close()

    actions
  }

  def solution(actions: List[Action], limit: Int = Int.MaxValue): Long = {
    val data: Data = Data(actions, limit)

    val coord = data.compressedCoordinates.toCoord
    val state = new State(coord)

    data.actions.foreach(state.applyAction)

    state.countOn
  }

  def part1(actions: List[Action]): Long = {
    solution(actions, 50)
  }

  def part2(actions: List[Action]): Long = {
    solution(actions)
  }

  println("----(AOC2021 - Day 22)-----------------------[Scala]----")

  val exemple = parseData("day22.exemple")
  println("Exemple :: Part 1 ====>     " + part1(exemple))
  println("Exemple :: Part 2 ====>     " + part2(exemple))
  println("--------------------------------------------------------")

  val input = parseData("day22.input")
  println("Input   :: Part 1 ====>     " + part1(input))
  println("Input   :: Part 2 ====>     " + part2(input))
  println("--------------------------------------------------------")
}
