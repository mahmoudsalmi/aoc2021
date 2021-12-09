#   0:      1:      2:      3:      4:
#  aaaa    ....    aaaa    aaaa    ....
# b    c  .    c  .    c  .    c  b    c
# b    c  .    c  .    c  .    c  b    c
#  ....    ....    dddd    dddd    dddd
# e    f  .    f  e    .  .    f  .    f
# e    f  .    f  e    .  .    f  .    f
#  gggg    ....    gggg    gggg    ....

#   5:      6:      7:      8:      9:
#  aaaa    aaaa    aaaa    aaaa    aaaa
# b    .  b    .  .    c  b    c  b    c
# b    .  b    .  .    c  b    c  b    c
#  dddd    dddd    ....    dddd    dddd
# .    f  e    f  .    f  e    f  .    f
# .    f  e    f  .    f  e    f  .    f
#  gggg    gggg    ....    gggg    gggg


def segment_to_char(segment):
    return chr(ord('a') + segment - 1)


def char_to_segment(char):
    return ord(char) - ord('a') + 1


def segment_to_pow2(segment):
    return 1 << (7 - segment)


def char_to_pow2(char):
    if char == '-':
        return 0
    return segment_to_pow2(char_to_segment(char))


def segment_is_active(digit, segment):
    return ((digit >> (7 - segment)) & 1) == 1


def count_segments(digit):
    return len(list(filter(
        lambda active: active,
        map(lambda s: segment_is_active(digit, s), range(1, 8))
    )))


DIGITS = [
    0b_1_1_1_0_1_1_1,
    0b_0_0_1_0_0_1_0,
    0b_1_0_1_1_1_0_1,
    0b_1_0_1_1_0_1_1,
    0b_0_1_1_1_0_1_0,
    0b_1_1_0_1_0_1_1,
    0b_1_1_0_1_1_1_1,
    0b_1_0_1_0_0_1_0,
    0b_1_1_1_1_1_1_1,
    0b_1_1_1_1_0_1_1
]
DIGITS_SEGMENTS_COUNTS = list(map(count_segments, DIGITS))


def map_digits_using_segments_count(digits_str):
    res = []
    for i in range(10):
        digit_map = []
        for digit_str in digits_str:
            if len(digit_str) == DIGITS_SEGMENTS_COUNTS[i]:
                digit_map.append(digit_str)
        res.append(digit_map)
    return res


def only_in_first(first, second):
    return list(filter(lambda char: char not in second, first))


def remove_char(digits_map, excluded_char):
    ds_map = []
    for digit_map in digits_map:
        d_map = []
        for segment_chars in digit_map:
            chars = []
            for char in segment_chars:
                if char != excluded_char:
                    chars.append(char)
            d_map.append(chars)
        ds_map.append(d_map)
    return ds_map


def remove_chars(digits_map, excluded_chars):
    d_map = digits_map
    for excluded_char in excluded_chars:
        d_map = remove_char(d_map, excluded_char)
    return d_map


def disable_segment(digit, segment):
    if digit & segment_to_pow2(segment) == 0:
        return digit
    return digit ^ segment_to_pow2(segment)


def calculate_remaining_segments(segments):
    res = DIGITS
    for segment in segments:
        new_res = []
        for digit in res:
            new_res.append(disable_segment(digit, segment))
        res = new_res
    return list(map(count_segments, res))


def clean_digits_maps(digits_map, segments):
    remaining_segments = calculate_remaining_segments(list(map(char_to_segment, segments)))
    d_map = []
    for digit, digit_map in enumerate(digits_map):
        d_map.append(list(
            filter(lambda d, dig=digit: len(d) == remaining_segments[dig], digit_map)
        ))
    return d_map


def mapping_segment(digits_str):
    digits_map = map_digits_using_segments_count(digits_str)

    a = only_in_first(digits_map[7][0], digits_map[1][0])[0]
    digits_map = remove_char(digits_map, a)

    f = digits_map[1][0][0]
    digits_map = remove_char(digits_map, f)

    c = digits_map[1][0][0]
    digits_map = remove_char(digits_map, c)

    digits_map = clean_digits_maps(digits_map, ['a', 'f', 'c'])

    b = only_in_first(digits_map[4][0], digits_map[3][0])[0]
    digits_map = remove_char(digits_map, b)

    g = only_in_first(digits_map[3][0], digits_map[4][0])[0]
    digits_map = remove_char(digits_map, g)

    d = digits_map[3][0][0]
    digits_map = remove_char(digits_map, d)

    e = digits_map[6][0][0]

    digits_map = map_digits_using_segments_count(digits_str)
    digits_map = remove_chars(digits_map, [a, b, d, e, g])
    digits_map = clean_digits_maps(digits_map, ['a', 'b', 'd', 'e', 'g'])

    f = digits_map[6][0][0]
    digits_map = remove_char(digits_map, f)

    c = digits_map[1][0][0]

    return {
        a: char_to_pow2('a'),
        b: char_to_pow2('b'),
        c: char_to_pow2('c'),
        d: char_to_pow2('d'),
        e: char_to_pow2('e'),
        f: char_to_pow2('f'),
        g: char_to_pow2('g'),
    }


def calculate_digit(segment_map, digit_str):
    calculate_digit_value = 0
    for segment_str in digit_str:
        calculate_digit_value += segment_map[segment_str]
    for digit, digit_value in enumerate(DIGITS):
        if digit_value == calculate_digit_value:
            return str(digit)
    return str(0)


def calculate_number(segment_map, number_str):
    return int("".join(list(
        map(lambda ds: calculate_digit(segment_map, ds), number_str)
    )))


def parse_data(filename):
    return list(map(
        lambda line: list(map(
            lambda token: list(map(sorted, token.split(' '))),
            line.replace("\n", "").split(" | ")
        )),
        open(filename, 'r').readlines()
    ))


def part1(data):
    res = 0
    for digits in map(lambda line: line[1], data):
        for digit in digits:
            if len(digit) in [DIGITS_SEGMENTS_COUNTS[1], DIGITS_SEGMENTS_COUNTS[4], DIGITS_SEGMENTS_COUNTS[7], DIGITS_SEGMENTS_COUNTS[8]]:
                res += 1
    return res


def part2_line(line):
    segment_map = mapping_segment(line[0])
    number = calculate_number(segment_map, line[1])
    return number


def part2(data):
    return sum(
        list(
            map(part2_line, data)
        )
    )


print("----(AOC2021 - Day 08)-------------------------[Py3]----")

exemple = parse_data("day8.exemple")
print(f"Exemple :: Part 1 ====>     {part1(exemple)}")
print(f"Exemple :: Part 2 ====>     {part2(exemple)}")
print("--------------------------------------------------------")

input_data = parse_data("day8.input")
print(f"Input   :: Part 1 ====>     {part1(input_data)}")
print(f"Input   :: Part 2 ====>     {part2(input_data)}")
print("--------------------------------------------------------")
