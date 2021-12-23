import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.reflect.ClassTag

object day18 extends App {

  type DataType = Char | Int
  type Data = List[DataType]

  object Operation extends Enumeration {
    type Operation = Value
    val Explode, Split, NoOp = Value
  }

  import Operation._

  def takeElement(string: String, data: Data): (String, Data) = string match {
    case blank if blank.isBlank => throw Exception("Unreachable!")
    case digit if digit.charAt(0).isDigit => Some(digit.indexWhere(!_.isDigit))
      .map(idx => (
        digit.slice(idx, digit.length),
        data ::: List(digit.slice(0, idx).toInt))
      )
      .get
    case str => (
      str.slice(1, str.length),
      data ::: List(str.charAt(0))
    )
  }

  @tailrec
  def takeElements(string: String, data: Data = List.empty): (String, Data) = string match {
    case end if end.isBlank => (end, data)
    case str => takeElement(str, data) match {
      case (s, d) => takeElements(s, d)
    }
  }

  def parseLine(line: String): Data = takeElements(line)._2.filter(a => a != ',')

  def parseData(filename: String): List[Data] = {
    val source = Source.fromFile(filename)
    val res = source.getLines().map(parseLine).toList
    source.close()
    res
  }

  def getFirstPairToExplode(data: Data): Option[(Operation, Int)] = {
    var level = 0
    data.sliding(2).zipWithIndex.foreach {
      case (pair, idx): (Data, Int) =>
        pair.head match {
          case i: Int =>
            if (level > 4 && pair.count(e => e.isInstanceOf[Int]) == 2) return Some((Explode, idx))
          case c: Char =>
            if (c == '[') level += 1
            else if (c == ']') level -= 1
        }
    }
    None
  }


  def getFirstElementToSplit(data: Data): Option[(Operation, Int)] = {
    data.sliding(2).zipWithIndex.foreach {
      case (pair, idx): (Data, Int) =>
        pair.head match {
          case i: Int if i > 9 => return Some((Split, idx))
          case _ =>
        }
    }
    None
  }

  def getFirstIdxToReduce(data: Data): (Operation, Int) =
    getFirstPairToExplode(data) match {
      case Some(toExplode) => toExplode
      case None => getFirstElementToSplit(data) match {
        case Some(toSplit) => toSplit
        case None => (NoOp, -1)
      }
    }

  def splitValue(data: Data, idx: Int): Seq[DataType] =
    Some(data(idx).asInstanceOf[Int])
      .map((v: Int) => Seq[DataType]('[', Math.floor(v.toFloat / 2).toInt, Math.ceil(v.toFloat / 2).toInt, ']'))
      .get

  def split(data: Data, idx: Int): Data = data.patch(idx, splitValue(data, idx), 1)

  def explodeLeft(data: Data, value: Int): Data = {
    val lastIndex = data.length - 1
    Some(data.lastIndexWhere(_.isInstanceOf[Int])).map {
      case -1 => data
      case idx => data.patch(idx, Seq((data(idx).asInstanceOf[Int] + value).toInt), 1)
    }.get
  }

  def explodeRight(data: Data, value: Int): Data =
    Some(data.indexWhere(_.isInstanceOf[Int])).map {
      case -1 => data
      case idx => data.patch(idx, Seq((data(idx).asInstanceOf[Int] + value).toInt), 1)
    }.get

  def explode(data: Data, idx: Int): Data = List.concat(
    explodeLeft(data.slice(0, idx - 1), data(idx).asInstanceOf[Int]),
    List(0),
    explodeRight(data.slice(idx + 3, data.length), data(idx + 1).asInstanceOf[Int])
  )

  def reduceData(data: Data): Data = Some(getFirstIdxToReduce(data)).map {
    case (NoOp, _) => data
    case (Split, idx: Int) => reduceData(split(data, idx))
    case (Explode, idx: Int) => reduceData(explode(data, idx))
  }.get

  def add(d1: Data, d2: Data): Data = reduceData(List('[') ::: reduceData(d1) ::: reduceData(d2) ::: List(']'))

  def getIndexOfNumberPair(data: Data): Int =
    data
      .map {
        case i: Int => 1
        case c: Char => 0
      }
      .indexOfSlice(Seq(1, 1))

  @tailrec
  def magnitude(data: Data): Int = getIndexOfNumberPair(data) match {
    case -1 => data.head.asInstanceOf[Int]
    case idx => magnitude(
      data.patch(
        idx - 1,
        Seq(data(idx).asInstanceOf[Int] * 3 + data(idx + 1).asInstanceOf[Int] * 2),
        4
      )
    )
  }

  def part1(data: List[Data]): Int = magnitude(data.reduce(add))

  def part2(data: List[Data]): Int = {
    (for (i <- data.indices; j <- data.indices) yield (i, j))
      .map {
        case (i, j) if i != 0 => List(
          magnitude(add(data(i), data(j))),
          magnitude(add(data(j), data(i)))
        ).max
        case _ => Int.MinValue
      }.max
  }

  println("----(AOC2021 - Day 18)-----------------------[Scala]----")

  val exemple = parseData("day18.exemple")
  println("Exemple :: Part 1 ====>     " + part1(exemple))
  println("Exemple :: Part 2 ====>     " + part2(exemple))
  println("--------------------------------------------------------")

  val input = parseData("day18.input")
  println("Input   :: Part 1 ====>     " + part1(input))
  println("Input   :: Part 2 ====>     " + part2(input))
  println("--------------------------------------------------------")
}
