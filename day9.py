from pprint import pprint


def parse_data(filename):
    matrix = list(
        map(
            lambda line: list(map(int, list(line))),
            map(lambda l: l.replace('\n', ''), open(filename, 'r').readlines())
        )
    )
    return len(matrix), len(matrix[0]), matrix


def part1(data):
    h, w, m = data
    lowers_point = []
    for y in range(h):
        for x in range(w):
            v = m[y][x]
            locations = []
            if x > 0: locations.append(m[y][x - 1])
            if x < w - 1: locations.append(m[y][x + 1])
            if y > 0: locations.append(m[y - 1][x])
            if y < h - 1: locations.append(m[y + 1][x])
            if min(locations) > v: lowers_point.append((x, y, v))

    return sum(list(map(lambda p: p[2] + 1, lowers_point)))


def part2(data):
    return -1


print("----(AOC2021 - Day 09)-------------------------[Py3]----")

exemple = parse_data("day9.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
print(f"Exemple :: Part 2 ====>     {part2(exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day9.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
print(f"Input   :: Part 2 ====>     {part2(input_data)}")
print("--------------------------------------------------------")
