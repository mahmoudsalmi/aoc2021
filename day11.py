def parse_data(filename):
    matrix = list(
        map(
            lambda line: list(map(int, list(line))),
            map(lambda l: l.replace('\n', ''), open(filename, 'r').readlines())
        )
    )
    return len(matrix), len(matrix[0]), matrix


def get_adjacent_locations(x, y, h, w):
    dx1 = -1 if x > 0 else 0
    dy1 = -1 if y > 0 else 0
    dx2 = 1 if x < w - 1 else 0
    dy2 = 1 if y < h - 1 else 0
    locations = []
    for dx in range(dx1, dx2 + 1):
        for dy in range(dy1, dy2 + 1):
            if (dx, dy) != (0, 0):
                locations.append((x + dx, y + dy))
    return locations


def get_flash_points(h, w, m):
    flash_points = []
    for y in range(h):
        for x in range(w):
            if m[y][x] >= 10:
                flash_points.append((x, y))
    return flash_points


def do_flash_points(flash_points, h, w, m):
    for x, y in flash_points:
        m[y][x] = 0
        for ax, ay in get_adjacent_locations(x, y, h, w):
            if m[ay][ax] > 0:
                m[ay][ax] += 1


def next_step(h, w, m):
    for y in range(h):
        for x in range(w):
            m[y][x] += 1
    flash_points = get_flash_points(h, w, m)
    count_flashes = 0
    while flash_points:
        count_flashes += len(flash_points)
        do_flash_points(flash_points, h, w, m)
        flash_points = get_flash_points(h, w, m)
    return count_flashes


def part1(data):
    count_flashes = 0
    for _ in range(100):
        count_flashes += next_step(*data)
    return count_flashes


def part2(h, w, m):
    step = 1
    count_flashes = next_step(h, w, m)
    while count_flashes < h * w:
        step += 1
        count_flashes = next_step(h, w, m)
    return step


print("----(AOC2021 - Day 11)-------------------------[Py3]----")

exemple = parse_data("day11.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
exemple = parse_data("day11.exemple")
print(f"Exemple :: Part 2 ====>     {part2(*exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day11.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
input_data = parse_data("day11.input")
print(f"Input   :: Part 2 ====>     {part2(*input_data)}")
print("--------------------------------------------------------")
