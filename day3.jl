#=
    https://adventofcode.com/2021/day/0
=#

using DelimitedFiles

function parseData(line)
    width = length(line)
    res::Array{Int64} = zeros(width)
    for i in 1:width
        res[i] = line[i] == '1' ? 1 : 0 
    end
    return res
end

function toInt(a)
    width = length(a)
    res = 0
    for i in 1:width
        res = res + a[i] * 2^(width - i)
    end
    return res
end

function mostAndLeast(data)
    s = length(data)
    width = length(data[1])
    cumul = zeros(width)

    for i in 1:s
        cumul = cumul + parseData(data[i, 1])
    end
    

    return ( map(x -> x * 2 >= s ? 1 : 0, cumul), map(x -> x * 2 < s ? 1 : 0, cumul) )
    
end

function solution(data)
    res = map(toInt, mostAndLeast(data[:,1]))
    return (res[1], res[2], res[1] * res[2])
end 

function solution2(data)
    width = length(data[1])
    (most, least) = mostAndLeast(data[:,1])

    i = 1
    mostData = data
    while length(mostData) > 1 && i <= width 
        mostData = filter(x -> parseData(x)[i] == most[i], mostData)
        (most, _) = mostAndLeast(mostData)
        i = i + 1
    end

    j = 1
    leastData = data
    while length(leastData) > 1 && j <= width 
        leastData = filter(x -> parseData(x)[j] == least[j], leastData)
        (_, least) = mostAndLeast(leastData)
        j = j + 1
    end

    res = (mostData[1], leastData[1])
    res = map(parseData, res)
    res = map(toInt, res)
    return (res[1], res[2], res[1] * res[2])
end

println("----(AOC2021 - Day 03)-----------------------[Julia]----")

exemple = readdlm("day3.exemple", String);
println("Exemple :: Part 1 ====>     ", solution(exemple))
println("Exemple :: Part 2 ====>     ", solution2(exemple))
println("--------------------------------------------------------")

input = readdlm("day3.input", String);
println("Input   :: Part 1 ====>     ", solution(input))
println("Input   :: Part 2 ====>     ", solution2(input))
println("--------------------------------------------------------")
