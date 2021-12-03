#=
    https://adventofcode.com/2021/day/0
=#

using DelimitedFiles

exemple = readdlm("_day.exemple");
input = readdlm("_day.input");

function solution(d)
    return string("Dummy part1 : ", length(d))
end

function solution2(d)
    return string("Dummy part2 : ", length(d))
end

println("----(AOC2021 - Day 00)-----------------------[Julia]----")
println("Exemple :: Part 1 ====>     ", solution(exemple))
println("Exemple :: Part 2 ====>     ", solution2(exemple))
println("--------------------------------------------------------")
println("Input   :: Part 1 ====>     ", solution(input))
println("Input   :: Part 2 ====>     ", solution2(input))
println("--------------------------------------------------------")
