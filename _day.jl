#=
    https://adventofcode.com/2021/day/1
=#

using DelimitedFiles

exemple = readdlm("day1.exemple");
input = readdlm("day1.input");

function solution(d)
    res = 0
    for i in 2:length(d)
        if (d[i][1] > d[i - 1][1])
            res = res + 1
        end
    end
    return res
end

function solution2(d)
    byThreeData = zeros(length(d) - 2)
    for j in 1:length(byThreeData)
        byThreeData[j] = d[j] + d[j+1] + d[j+2]
    end
    return solution(byThreeData)
end

println("----(AOC2021 - Day1)-------------------------[Julia]----")
println("Exemple :: Part 1 ====>     ", solution(exemple))
println("Exemple :: Part 2 ====>     ", solution2(exemple))
println("--------------------------------------------------------")
println("Input   :: Part 1 ====>     ", solution(input))
println("Input   :: Part 2 ====>     ", solution2(input))
println("--------------------------------------------------------")
