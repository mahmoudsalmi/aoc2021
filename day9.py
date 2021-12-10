from pprint import pprint


def parse_data(filename):
    matrix = list(
        map(
            lambda line: list(map(int, list(line))),
            map(lambda l: l.replace('\n', ''), open(filename, 'r').readlines())
        )
    )
    return len(matrix), len(matrix[0]), matrix


def get_adjacent_locations(x, y, h, w, m):
    locations = []
    if x > 0:
        locations.append(
            (x - 1, y, m[y][x - 1])
        )
    if x < w - 1:
        locations.append(
            (x + 1, y, m[y][x + 1])
        )
    if y > 0:
        locations.append(
            (x, y - 1, m[y - 1][x])
        )
    if y < h - 1:
        locations.append(
            (x, y + 1, m[y + 1][x])
        )
    return locations


def get_risk_locations(x, y, v, h, w, m):
    return list(
        filter(
            lambda l: 9 > l[2] > v,
            get_adjacent_locations(x, y, h, w, m)
        )
    )


def get_lowers_points(h, w, m):
    lowers_point = []
    for y in range(h):
        for x in range(w):
            v = m[y][x]
            if min([l[2] for l in get_adjacent_locations(x, y, h, w, m)]) > v:
                lowers_point.append((x, y, v))
    return lowers_point


def get_bassin(data, point, bassin):
    bassin.append(point)
    for location in get_risk_locations(*point, *data):
        if location not in bassin:
            get_bassin(data, location, bassin)


def part1(data):
    return sum([p[2] + 1 for p in get_lowers_points(*data)])


def part2(data):
    bassin_sizes = []
    for point in get_lowers_points(*data):
        bassin = []
        get_bassin(data, point, bassin)
        bassin_sizes.append(len(bassin))
        bassin_sizes.sort(reverse=True)
        if len(bassin_sizes) > 3: bassin_sizes = bassin_sizes[:3]
    return bassin_sizes[0] * bassin_sizes[1] * bassin_sizes[2]


print("----(AOC2021 - Day 09)-------------------------[Py3]----")

exemple = parse_data("day9.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
print(f"Exemple :: Part 2 ====>     {part2(exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day9.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
print(f"Input   :: Part 2 ====>     {part2(input_data)}")
print("--------------------------------------------------------")
