const {createInterface} = require('readline');
const {createReadStream} = require('fs');
const {once} = require('events');

function parseLine(line) {
  return [...line].map(d => parseInt(d));
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

function mostAndLeast(data) {
  const len = data.length;
  const width = data[0].length;

  let res = data.reduce(
    (d, sum) => sum.map((r, i) => r + d[i]), 
    Array(width).fill(0)
  );
  
  return [ res.map(r => r * 2 >= len ? 1 : 0), res.map(r => r * 2 < len ? 1 : 0) ]
    .map( bits => bits.reduce((b, s) => b + s, ''))
}

function solution(data) {
  const res = mostAndLeast(data).map(s => parseInt(s, 2));
  return [...res, res.reduce((n, m) => n * m, 1)];
}

function solution2(data) {
  const width = data[0].length;
  let [most, least] = mostAndLeast(data);
  let res = [['0'], ['0']]
  
  for(let i = 0, mostData = data; i < width && mostData.length > 1; i++) {
    mostData = mostData.filter(line => String(line[i]) === String(most[i]));
    [most, ] = mostAndLeast(mostData)
    res[0] = mostData[0]
  }

  for(let i = 0, leastData = data; i < width && leastData.length > 1; i++) {
    leastData = leastData.filter(line => String(line[i]) === String(least[i]));
    [, least] = mostAndLeast(leastData)
    res[1] = leastData[0]
  }

  res = res.map(s => parseInt(s.join(''), 2));

  return [...res, res.reduce((n, m) => n * m, 1)];
}

(async function() {
  console.log("----(AOC2021 - Day 03)--------------------------[Js]----")
  
  const exemple = await parseFile('day3.exemple');
  console.log("Exemple :: Part 1 ====>     ", solution(exemple))
  console.log("Exemple :: Part 2 ====>     ", solution2(exemple))
  console.log("--------------------------------------------------------")
  
  const input = await parseFile('day3.input');
  console.log("Input   :: Part 1 ====>     ", solution(input))
  console.log("Input   :: Part 2 ====>     ", solution2(input))
  console.log("--------------------------------------------------------")
}());
