import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class day5 {
    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static class Line {
        public Point p1;
        public Point p2;

        public Line(String l) {
            List<Integer> coordinates = Arrays.stream(l.split(" -> "))
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

            this.p1 = new Point(coordinates.get(0), coordinates.get(1));
            this.p2 = new Point(coordinates.get(2), coordinates.get(3));
        }

        @Override
        public String toString() {
            return "Line{ " + p1 + " to " + p2 + " }";
        }

        public boolean isDiagonal() {
            return this.p1.x != this.p2.x && this.p1.y != this.p2.y;
        }
    }

    public static List<Line> parseData(String filename) {
        List<Line> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(new Line(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<Point> getAllPoints(Line line) {
        List<Point> res = new ArrayList<>();
        int yStep = line.p1.y < line.p2.y ? +1 : -1;
        int yMax = Integer.max(line.p1.y, line.p2.y);
        int yMin = Integer.min(line.p1.y, line.p2.y);

        int xStep = line.p1.x < line.p2.x ? +1 : -1;
        int xMax = Integer.max(line.p1.x, line.p2.x);
        int xMin = Integer.min(line.p1.x, line.p2.x);

        if (line.p1.x == line.p2.x) {
            for (int y = yMin; y <= yMax; y++) {
                res.add(new Point(xMax, y));
            }
        } else if (line.p1.y == line.p2.y) {
            for (int x = xMin; x <= xMax; x++) {
                res.add(new Point(x, yMax));
            }
        } else { // 45Deg
            for (
                int x = line.p1.x, y = line.p1.y;
                x >= xMin && x <= xMax && y >= yMin && y <= yMax;
                x += xStep, y += yStep
            ) {
                res.add(new Point(x, y));
            }
        }
        return res;
    }

    public static int part1(List<Line> lines) {
        Set<Point> points = new HashSet<>();
        Set<Point> duplicates = lines.stream()
            .filter(line -> !line.isDiagonal())
            .flatMap(line -> getAllPoints(line).stream())
            .filter(n -> !points.add(n))
            .collect(Collectors.toSet());
        return duplicates.size();
    }

    public static int part2(List<Line> lines) {
        Set<Point> points = new HashSet<>();
        Set<Point> duplicates = lines.stream()
            .flatMap(line -> getAllPoints(line).stream())
            .filter(n -> !points.add(n))
            .collect(Collectors.toSet());
        return duplicates.size();
    }

    public static void main(String[] args) {
        System.out.println("----(AOC2021 - Day 05)------------------------[Java]----");

        List<Line> exemple = parseData("day5.exemple");
        System.out.println("Exemple :: Part 1 ====>     " + part1(exemple));
        System.out.println("Exemple :: Part 2 ====>     " + part2(exemple));
        System.out.println("--------------------------------------------------------");

        List<Line> input = parseData("day5.input");
        System.out.println("Input   :: Part 1 ====>     " + part1(input));
        System.out.println("Input   :: Part 2 ====>     " + part2(input));
        System.out.println("--------------------------------------------------------");

    }
}
