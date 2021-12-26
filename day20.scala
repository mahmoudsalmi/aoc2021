import scala.annotation.tailrec
import scala.io.Source

object day20 extends App {

  case class Algo(values: IndexedSeq[Int]) {
    def has(value: Int): Boolean = values.contains(value)
  }

  type Points = IndexedSeq[Point]

  case class Point(x: Int, y: Int) {
    def getNeighbors: Points = for
      yy <- y - 1 to y + 1
      xx <- x - 1 to x + 1
    yield Point(xx, yy)
  }

  case class MinMaxPoints(min: Point, max: Point)

  case class Image(points: Points) {
    private val minmax: MinMaxPoints = minMaxPoints(points)

    def min: Point = minmax.min
    def max: Point = minmax.max

    def minX: Int = minmax.min.x
    def maxX: Int = minmax.max.x

    def minY: Int = minmax.min.y
    def maxY: Int = minmax.max.y

    def nextRange: Points = for
      x <- minX - 1 to maxX + 1
      y <- minY - 1 to maxY + 1
    yield Point(x, y)

    def pointInside(point: Point): Boolean =
      point.x >= minX
        && point.x <= maxX
        && point.y >= minY
        && point.y <= maxY

    def pointOutside(point: Point): Boolean = !pointInside(point)

    def has(point: Point): Boolean = points.contains(point)

    def countLit: Int = points.length
  }

  case class Data(algo: Algo, image: Image)

  def line2litIndexes(line: String): IndexedSeq[Int] = line.chars()
    .toArray
    .zipWithIndex
    .filter(ci => ci(0) == '#')
    .map(ci => ci(1))

  def parseAlgo(line: String): Algo = Algo(line2litIndexes(line).toIndexedSeq)

  def parseImage(lines: List[String]): Image = Image(
    lines.zipWithIndex.flatMap {
      case (line, y) => line2litIndexes(line).map(x => Point(x, y))
    }.toIndexedSeq
  )

  def parseData(filename: String): Data = {
    val source = Source.fromFile(filename)
    val lines = source.getLines().toList
    source.close()
    Data(parseAlgo(lines.head), parseImage(lines.tail.tail))
  }

  def minPoint(img: Points): Point = img.reduce((a, b) => Point(math.min(a.x, b.x), math.min(a.y, b.y)))

  def maxPoint(img: Points): Point = img.reduce((a, b) => Point(math.max(a.x, b.x), math.max(a.y, b.y)))

  def minMaxPoints(img: Points): MinMaxPoints = MinMaxPoints(minPoint(img), maxPoint(img))

  def pointToBinary(point: Point, image: Image, algo: Algo, step: Int): String =
    point.getNeighbors
      .map(neighbor => {
        if (image.pointOutside(neighbor)) {
          if algo.has(0) && step % 2 == 1 then "1" else "0"
        } else {
          if image.has(neighbor) then "1" else "0"
        }
      })
      .mkString

  def pointToAlgo(point: Point, image: Image, algo: Algo, step: Int): Int = Integer.parseInt(pointToBinary(point, image, algo, step), 2)

  def isLit(point: Point, image: Image, algo: Algo, step: Int): Boolean = algo.has(pointToAlgo(point, image, algo, step))

  def calculateNextImage(image: Image, algo: Algo, step: Int): Image = {
    Image(
      image.nextRange
        .filter(p => isLit(p, image, algo, step))
    )
  }

  @tailrec
  def nextImage(image: Image, algo: Algo, max: Int, step: Int = 0): Image =
    if step == max
    then {
      println()
      image
    } else {
      print(".")
      nextImage(calculateNextImage(image, algo, step), algo, max, step + 1)
    }

  def part1(data: Data): Int = nextImage(data.image, data.algo, 2).countLit

  def part2(data: Data): Int = nextImage(data.image, data.algo, 50).countLit

  println("----(AOC2021 - Day 20)-----------------------[Scala]----")

  val exemple = parseData("day20.exemple")
  println("Exemple :: Part 1 ====>     " + part1(exemple))
  println("Exemple :: Part 2 ====>     " + part2(exemple))
  println("--------------------------------------------------------")

  val input = parseData("day20.input")
  println("Input   :: Part 1 ====>     " + part1(input))
  println("Input   :: Part 2 ====>     " + part2(input))
  println("--------------------------------------------------------")
}
