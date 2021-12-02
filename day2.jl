using DelimitedFiles

data = readdlm("day1.input")[:,1];
byThreeData = zeros(length(data) - 2)
for j in 1:length(byThreeData)
    byThreeData[j] = data[j] + data[j+1] + data[j+2]
end

function solution(d)
    res = 0
    for i in 2:length(d)
        if (d[i][1] > d[i - 1][1])
            res = res + 1
        end
    end
    return res
end


println(solution(data))
println(solution(byThreeData))
