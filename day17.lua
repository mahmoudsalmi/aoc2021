function parseData(filename)
    local file = assert(io.open(filename, "r"))
    local line = file:read("*line")
    file:close()

    local res = {}
    for k in string.gmatch(line, "[-]*%d+") do
        table.insert(res, tonumber(k))
    end

    return res
end

function onTarget(tXMin, tXMax, tYMin, tYMax, vx0, vy0)
    local x, y = 0, 0
    local vx, vy = vx0, vy0
    local yMax = 0

    while
    math.abs(x) < math.max(math.abs(tXMin), math.abs(tXMax))
            and
            y > tYMin
    do
        x = x + vx
        y = y + vy

        vy = vy - 1
        if vx ~= 0 then
            vx = vx - (math.abs(vx) / vx)
        end
        yMax = math.max(yMax, y)

        if x <= tXMax
                and x >= tXMin
                and y <= tYMax
                and y >= tYMin
        then
            return true, yMax
        end
    end

    return false, 0
end

function solution(tXMin, tXMax, tYMin, tYMax)
    local res = {}
    res['numSolution'] = 0
    res['yMax'] = -999999999
    local vYMax = math.max(math.abs(tYMin), math.abs(tYMax))

    local vXStep = math.abs(tXMin) / tXMin
    local vXMax = vXStep * math.max(math.abs(tXMin), math.abs(tXMax))

    for vx0 = vXStep, vXMax, vXStep do
        for vy0 = tYMin, vYMax do
            local otOk, otYMax = onTarget(tXMin, tXMax, tYMin, tYMax, vx0, vy0)
            if otOk then
                res['numSolution'] = res['numSolution'] + 1
                res['yMax'] = math.max(otYMax, res['yMax'])
            end
        end
    end
    return res
end

function part1(data)
    local tXMin, tXMax, tYMin, tYMax = data[1], data[2], data[3], data[4]
    return solution(tXMin, tXMax, tYMin, tYMax)['yMax']
end

function part2(data)
    local tXMin, tXMax, tYMin, tYMax = data[1], data[2], data[3], data[4]
    return solution(tXMin, tXMax, tYMin, tYMax)['numSolution']
end


print("----(AOC2021 - Day 17)-------------------------[lua]----")

exemple = parseData('day17.exemple')
print("Exemple :: Part 1 ====>     " .. part1(exemple))
print("Exemple :: Part 2 ====>     " .. part2(exemple))
print("--------------------------------------------------------")

input = parseData('day17.input')
print("Input   :: Part 1 ====>     " .. part1(input))
print("Input   :: Part 2 ====>     " .. part2(input))
print("--------------------------------------------------------")
