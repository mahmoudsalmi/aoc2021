#=
    https://adventofcode.com/2021/day/2
=#

using DelimitedFiles

function getOp(dir, steps)
    if dir == "up"
        return (-steps , 0)
    elseif dir == "down"
        return (steps , 0)
    elseif dir == "forward"
        return (0 , steps)
    end
    return (0, 0)
end

function solution(data)
    res = (0, 0)
    for i in 1:size(data)[1]
        op = getOp(data[i, 1], data[i, 2])
        res = (res[1] + op[1], res[2] + op[2])
    end
    return (res[1], res[2], res[1] * res[2])
end

function solution2(data)
    res = (0, 0, 0)
    for i in 1:size(data)[1]
        (aim, dist) = getOp(data[i, 1], data[i, 2])
        if aim != 0
            res = (res[1], res[2], res[3] + aim)
        end

        if dist != 0
            res = ( res[1] + dist, res[2] + dist * res[3], res[3])
        end
    end
    return (res[1], res[2], res[1] * res[2])
end

exemple = readdlm("day2.exemple");
input = readdlm("day2.input");

println("----(AOC2021 - Day2)-------------------------[Julia]----")
println("Exemple :: Part 1 ====>     ", solution(exemple))
println("Exemple :: Part 2 ====>     ", solution2(exemple))
println("--------------------------------------------------------")
println("Input   :: Part 1 ====>     ", solution(input))
println("Input   :: Part 2 ====>     ", solution2(input))
println("--------------------------------------------------------")
