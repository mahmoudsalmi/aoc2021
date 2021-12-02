const {createInterface} = require('readline');
const {createReadStream} = require('fs');
const {once} = require('events');

async function parseFile(filename) {
  const input = createReadStream(filename);
  const rl = createInterface({input, crlfDelay: Infinity});

  const data = [];

  rl.on('line', (line) => {
    data.push(parseInt(line))
  });

  await once(rl, 'close');
  return data;
}

function solution(data) {
  return data
    .map((v, i, d) => i === 0 ? false : v > d[i - 1])
    .filter(inc => inc)
    .length;
}

function solution2(data) {
  return solution(
    data
      .map((v, i, input) => i > input.length - 2 ? null : v + input[i + 1] + input[i + 2])
      .filter(v => v !== null)
  );
}

(async function() {
  const exemple = await parseFile('day1.exemple');
  const input = await parseFile('day1.input');

  console.log("----(AOC2021 - Day1)----------------------------[Js]----")
  console.log("Exemple :: Part 1 ====>     ", solution(exemple))
  console.log("Exemple :: Part 2 ====>     ", solution2(exemple))
  console.log("--------------------------------------------------------")
  console.log("Input   :: Part 1 ====>     ", solution(input))
  console.log("Input   :: Part 2 ====>     ", solution2(input))
  console.log("--------------------------------------------------------")
}());
