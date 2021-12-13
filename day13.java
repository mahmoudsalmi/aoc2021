import java.io.*;
import java.util.*;

public class day13 {
    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean fold(Fold f) {
            if (f.direction == 'x') {
                return foldX(f.position);
            } else if (f.direction == 'y') {
                return foldY(f.position);
            }
            return false;
        }

        public boolean foldX(int fx) {
            if (x == fx) {
                return true;
            }

            if (x > fx) {
                x = 2 * fx - x;
            }

            return false;
        }

        public boolean foldY(int fy) {
            if (y == fy) {
                return true;
            }

            if (y > fy) {
                y = 2 * fy - y;
            }

            return false;
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
    public static class Fold {
        char direction;
        int position;

        public Fold(char direction, int position) {
            this.direction = direction;
            this.position = position;
        }

        @Override
        public String toString() {
            return "Fold{" +
                "direction=" + direction +
                ", position=" + position +
                '}';
        }
    }
    public static class Data {
        List<Point> points = new ArrayList<>();
        List<Fold> folds = new ArrayList<>();

        public void parseLine(String line) {
            if (line.startsWith("fold along y=")) {
                folds.add(
                    new Fold('y', Integer.parseInt(line.replace("fold along y=", "")))
                );
            } else if (line.startsWith("fold along x=")) {
                folds.add(
                    new Fold('x', Integer.parseInt(line.replace("fold along x=", "")))
                );
            } else {
                String[] coordinates = line.split(",");
                if (coordinates.length == 2) {
                    List<Integer> coor = Arrays.stream(coordinates).map(Integer::parseInt).toList();
                    this.points.add(new Point(coor.get(0), coor.get(1)));
                }
            }
        }

        public void fold() {
            Fold f = this.folds.remove(0);
            List<Point> toDelete = new ArrayList<>();
            for (Point point : points) {
                boolean delete = point.fold(f);
                if (delete) toDelete.add(point);
            }
            toDelete.forEach(p -> points.remove(p));
                this.points = new HashSet<Point>(this.points).stream().toList();
        }

        public void foldAll() {
            while (!this.folds.isEmpty()) {
                this.fold();
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                "points=" + points +
                ", folds=" + folds +
                '}';
        }
    }

    public static Data parseData(String filename) {
        Data data = new Data();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void printPaper(List<Point> points, PrintStream out) {
        out.println();

        int xMax = points.stream().map(point -> point.x).max(Integer::compareTo).orElse(0);
        int yMax = points.stream().map(point -> point.y).max(Integer::compareTo).orElse(0);

        for (int y = 0; y <= yMax; y++) {
            for (int x = 0; x <= xMax; x++) {
                out.print(points.contains(new Point(x, y)) ? '#' : ' ');
            }
            out.println();
        }
        out.println();
    }

    public static int part1(Data data) {
        data.fold();
        return data.points.size();
    }

    public static String part2(Data data) {
        data.foldAll();
        printPaper(data.points, System.out);
        return "Look up ! ^^ ";
    }

    public static void main(String[] args) {
        System.out.println("----(AOC2021 - Day 13)------------------------[Java]----");

        Data exemple = parseData("day13.exemple");
        System.out.println("Exemple :: Part 1 ====>     " + part1(exemple));
        exemple = parseData("day13.exemple");
        System.out.println("Exemple :: Part 2 ====>     " + part2(exemple));
        System.out.println("--------------------------------------------------------");

        Data input = parseData("day13.input");
        System.out.println("Input   :: Part 1 ====>     " + part1(input));
        input = parseData("day13.input");
        System.out.println("Input   :: Part 2 ====>     " + part2(input));
        System.out.println("--------------------------------------------------------");

    }
}
