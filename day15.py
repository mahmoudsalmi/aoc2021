import numpy as np


def parse_data(filename):
    matrix = list(map(
        lambda line: list(map(int, list(line))),
        map(lambda l: l.replace('\n', ''), open(filename, 'r').readlines())
    ))
    return np.array(matrix)


def get_neighbors(u, shape):
    y, x = np.unravel_index(u, shape)
    y_max, x_max = shape
    res = []
    if x > 0:
        res.append((y, x - 1))
    if x < x_max - 1:
        res.append((y, x + 1))
    if y > 0:
        res.append((y - 1, x))
    if y < y_max - 1:
        res.append((y + 1, x))
    return list(map(lambda c: np.ravel_multi_index(c, shape), res))


def part1(data):
    start = 0
    end = data.size - 1

    dist = np.ones_like(data) * np.inf
    prev = np.full(data.shape, -1, dtype=np.int64)
    visited = np.full(data.shape, 1., dtype=float)  # 1 if not visited / inf if visited

    dist.flat[start] = 0

    while 1. in visited and prev.flat[end] == -1:
        u = np.argmin((dist + 1) * visited)
        for n in get_neighbors(u, data.shape):
            n_dist = dist.flat[u] + data.flat[n]
            visited[np.unravel_index(u, data.shape)] = 1. * np.inf
            if n_dist < dist.flat[n]:
                dist.flat[n] = n_dist
                prev.flat[n] = u
    return int(dist.flat[end])


def increment(i, steps):
    res = i
    for _ in range(steps):
        if res == 9:
            res = 1
        else:
            res += 1
    return res


v_increment = np.vectorize(increment)


def part2(data):
    large_data = []

    for i in range(5):
        large_data_line = v_increment(np.copy(data), i)
        for j in range(1, 5):
            large_data_line = np.concatenate([large_data_line, v_increment(np.copy(data), i + j)], axis=1)
        if i == 0:
            large_data = large_data_line
        else:
            large_data = np.concatenate([large_data, large_data_line], axis=0)

    print(large_data.shape)
    return part1(large_data)


print("----(AOC2021 - Day 15)-------------------------[Py3]----")

exemple = parse_data("day15.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
print(f"Exemple :: Part 2 ====>     {part2(exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day15.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
print(f"Input   :: Part 2 ====>     {part2(input_data)}")
print("--------------------------------------------------------")
