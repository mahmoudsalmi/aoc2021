CHUNK_OPENS = ['(', '[', '{', '<']
CHUNK_CLOSES = [')', ']', '}', '>']
CHUNK_POINTS = [3, 57, 1197, 25137]
CHUNK_POINTS_INCOMPLETE = [1, 2, 3, 4]


def parse_data(filename):
    return list(
        map(
            lambda line: list(line),
            map(lambda l: l.replace('\n', ''), open(filename, 'r').readlines())
        )
    )


def verify_corrupted_line(line):
    stack = []
    for c in line:
        if c in CHUNK_OPENS:
            stack.append(CHUNK_OPENS.index(c))
        if c in CHUNK_CLOSES:
            o = stack.pop()
            if CHUNK_CLOSES[o] != c:
                return CHUNK_POINTS[CHUNK_CLOSES.index(c)]
    return 0


def calculate_incomplete_line(stack):
    res = 0
    stack.reverse()
    for i in stack:
        res *= 5
        res += CHUNK_POINTS_INCOMPLETE[i]
    return res


def verify_incomplete_line(line):
    stack = []
    for c in line:
        if c in CHUNK_OPENS:
            stack.append(CHUNK_OPENS.index(c))
        if c in CHUNK_CLOSES:
            o = stack.pop()
            if CHUNK_CLOSES[o] != c:
                return 0
    if len(stack) == 0:
        return 0
    return calculate_incomplete_line(stack)


def part1(data):
    return sum(list(
        map(verify_corrupted_line, data)
    ))


def part2(data):
    scores = list(sorted(filter(
        lambda x: x > 0,
        map(verify_incomplete_line, data)
    )))
    return scores[int(len(scores) / 2)]


print("----(AOC2021 - Day 10)-------------------------[Py3]----")

exemple = parse_data("day10.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
print(f"Exemple :: Part 2 ====>     {part2(exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day10.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
print(f"Input   :: Part 2 ====>     {part2(input_data)}")
print("--------------------------------------------------------")
