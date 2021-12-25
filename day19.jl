function buildRotMatrices(angle = 90)
    local ns(a) = trunc(Int, -sind(a))
    local s(a) = trunc(Int, sind(a))
    local c(a) = trunc(Int, cosd(a))

    local rotX(a) = [1 0 0; 0 c(a) ns(a); 0 s(a) c(a)]
    local rotY(a) = [c(a) 0 s(a); 0 1 0; ns(a) 0 c(a)]
    local rotZ(a) = [c(a) ns(a) 0; s(a) c(a) 0; 0 0 1]

    local angleRange = 0:angle:359

    local initPosition = [1 2 3]
    local res = [rotX(0)]
    local positions = [initPosition]

    for xAngle in angleRange, yAngle in angleRange, zAngle in angleRange
        local rotation = rotX(xAngle) * rotY(yAngle) * rotZ(zAngle)
        local position = initPosition * rotation
        if position ∉ positions
            push!(positions, position)
            push!(res, rotation)
        end
    end
    return res
end

rot90Matrices = buildRotMatrices()
rot90Size = length(rot90Matrices)

rotKey = "rotation"
transKey = "transition"

function parseData(filename)
    local lines = readlines(filename)

    i = 0
    data = []
    while i <= length(lines)
        i += 1; line = lines[i]
        if startswith(line, "--- scanner ")
            local dataset = []
            i += 1; line = lines[i]
            while !isempty(line)
                push!(dataset, reshape(map(x -> parse(Int, x), split(line, ",")), 1, :))
                i += 1; line = i <= length(lines) ? lines[i] : ""
            end
            push!(data, dataset)
        end
    end
    return data
end

function verify12OverlappingPoints(originReport, transformedReport)
    count = 0
    for i in 1:length(transformedReport)
        if transformedReport[i] ∈ originReport
            count += 1
            if count >= 12
                 return true
            end
        end
    end
    return false
end

function calculateTransformations(originReport, report)
    for r in 1:rot90Size, j in 1:length(report)
        invRotation = rot90Matrices[r]
        rotatedReport = map(x -> x * invRotation, report)
        transitions = Dict()
        for i in 1:length(originReport)
            transition = originReport[i] - rotatedReport[j]
            if !haskey(transitions, transition)
                transitions[transition] = true

                transformedReport = map(x -> x + transition, rotatedReport)
                if verify12OverlappingPoints(originReport, transformedReport)
                    return (invRotation', transition)
                end
           end
        end
    end
    return Dict()
end

function buildGraph(data)
    dataSize = length(data)
    res = Dict()
    for i in 1:dataSize
        res[i] = Dict()
    end
    for i in 1:dataSize, j in 1:dataSize
        if i != j
            transformations = calculateTransformations(data[i], data[j])
            if length(transformations) > 0
                println(" (", i, ",", j, ")  => ", transformations)
                res[i][j] = transformations
            else

            end
        end
    end
    return res
end

function normalizeGraph(graph)
    normalizedGraph = Dict(1 => ([1 0 0; 0 1 0; 0 0 1], [0 0 0]))
    visited = []
    toVisit = [1]
    while length(toVisit) > 0
        currentScanner = pop!(toVisit)
        if currentScanner ∉ visited
            push!(visited, currentScanner)

            currentTransformation = normalizedGraph[currentScanner]
            currentRotation = currentTransformation[1]
            currentTransition = currentTransformation[2]

            for (nextScanner, nextTransformation) in graph[currentScanner]
                push!(toVisit, nextScanner)
                if !haskey(normalizedGraph, nextScanner)
                    nextRotation = nextTransformation[1]
                    nextTransition = nextTransformation[2]

                    normalizedGraph[nextScanner] = (
                        currentRotation * nextRotation,
                        currentTransition + (nextTransition / currentRotation)
                    )
                end
            end
        end
    end
    return normalizedGraph
end

function part1(data, transformations)
    res = Set()
    for i in 1:length(data)
        normalizedData = map( x -> x * transformations[i][1]' + transformations[i][2], data[i])
        for j in 1:length(normalizedData)
            push!(res, normalizedData[j])
        end
    end
    return length(res)
end

function part2(tr)
    max = -9999999999
    for i in 1:length(tr) - 1, j in i+1:length(tr)
        md = abs(tr[i][2][1] - tr[j][2][1]) + abs(tr[i][2][2] - tr[j][2][2]) +  + abs(tr[i][2][3] - tr[j][2][3])
        if md > max
             max = md
        end
    end
    return max
end


function solution()
    println("----(AOC2021 - Day 19)-----------------------[Julia]----")

    println("")
    println("")
    println("")

    println("--------------------------------------------------------")
    exemple = parseData("day19.exemple")
    exempleGraph = normalizeGraph(buildGraph(exemple))
    println("--------------------------------------------------------")
    println("Exemple :: Part 1 ====>     ", part1(exemple, exempleGraph))
    println("Exemple :: Part 2 ====>     ", part2(exempleGraph))
    println("--------------------------------------------------------")

    println("")
    println("")
    println("")

    input = parseData("day19.input")
    inputGraph = normalizeGraph(buildGraph(input))
    println()
    println("--------------------------------------------------------")
    println("Input   :: Part 1 ====>     ", part1(input, inputGraph))
    println("Input   :: Part 2 ====>     ", part2(inputGraph))
    println("--------------------------------------------------------")
end

solution()
