#=
    https://adventofcode.com/2021/day/1
=#

using DelimitedFiles

exemple = readdlm("_day.exemple");
input = readdlm("_day.input");

function solution(d)
    return "No result"
end

function solution2(d)
    return "No result"
end

println("----(AOC2021 - Day 00)-----------------------[Julia]----")
println("Exemple :: Part 1 ====>     ", solution(exemple))
println("Exemple :: Part 2 ====>     ", solution2(exemple))
println("--------------------------------------------------------")
println("Input   :: Part 1 ====>     ", solution(input))
println("Input   :: Part 2 ====>     ", solution2(input))
println("--------------------------------------------------------")
