const {createInterface} = require('readline');
const {createReadStream} = require('fs');
const {once} = require('events');

function parseLine(line) {
  return parseInt(line);
}

async function parseFile(filename) {
  const input = createReadStream(filename);
  const rl = createInterface({input, crlfDelay: Infinity});

  const data = [];

  rl.on('line', (line) => {
    data.push(parseLine(line))
  });

  await once(rl, 'close');
  return data;
}

function solution(data) {
  return `Dummy part1: ${data.length}`;
}

function solution2(data) {
  return `Dummy part2: ${data.length}`;
}

(async function() {
  const exemple = await parseFile('_day.exemple');
  const input = await parseFile('_day.input');

  console.log("----(AOC2021 - Day01)---------------------------[Js]----")
  console.log("Exemple :: Part 1 ====>     ", solution(exemple))
  console.log("Exemple :: Part 2 ====>     ", solution2(exemple))
  console.log("--------------------------------------------------------")
  console.log("Input   :: Part 1 ====>     ", solution(input))
  console.log("Input   :: Part 2 ====>     ", solution2(input))
  console.log("--------------------------------------------------------")
}());
