const {createInterface} = require('readline');
const {createReadStream} = require('fs');
const {once} = require('events');

class Packet {
  version = 0;
  type = 0;
  packets = [];
  value = 0;

  constructor(version, type) {
    this.version = version;
    this.type = type;
  }
}

function parseLine(line) {
  return [...line].map(x => parseInt(x, 16)
        .toString(2)
        .padStart(4, '0')
    ).join('');
}

async function parseFile(filename) {
  const input = createReadStream(filename);
  const rl = createInterface({input, crlfDelay: Infinity});

  const data = [];

  rl.on('line', (line) => {
    data.push(parseLine(line))
  });

  await once(rl, 'close');
  return data[0];
}

function takeBits(bits, size) {
  return [bits.slice(0, size), bits.slice(size)]
}

function takeBool(bits) {
  return [bits.slice(0, 1) === '1', bits.slice(1)]
}

function takeLiteral(bits) {
  let continueReading = true;
  let acc = '';
  let rest = bits;
  while (continueReading) {
    let b;
    [continueReading, rest] = takeBool(rest);
    [b, rest] = takeBits(rest, 4);
    acc += b;
  }
  return [parseInt(acc, 2), rest]
}

function takeNumber(bits, size) {
  return [parseInt(bits.slice(0, size), 2), bits.slice(size)]
}

function readPacket(bits) {
  let rest = bits;
  let version, type, modeNumberSubPackets;

  [version, rest] = takeNumber(rest, 3);
  [type, rest] = takeNumber(rest, 3);
  let packet = new Packet(version, type);

  if (type === 4) {
    [packet.value, rest] = takeLiteral(rest);
  } else {
    [modeNumberSubPackets, rest] = takeBool(rest);

    if (modeNumberSubPackets) {
      let numSubPackets;
      [numSubPackets, rest] = takeNumber(rest, 11);
      for (let i = 0; i < numSubPackets; i++) {
        let subPacket;
        [subPacket, rest] = readPacket(rest)
        packet.packets.push(subPacket);
      }
    } else {
      let subPacketsSize, subPacketsBits;
      [subPacketsSize, rest] = takeNumber(rest, 15);

      [subPacketsBits, rest] = takeBits(rest, subPacketsSize);
      while (subPacketsBits.length > 0) {
        let subPacket;
        [subPacket, subPacketsBits] = readPacket(subPacketsBits);
        packet.packets.push(subPacket);
      }
    }
  }

  return [packet, rest];
}

function sumVersions(packet) {
  let res = packet.version;

  if (packet.packets?.length) {
    for (const subPacket of packet.packets) {
      res += sumVersions(subPacket)
    }
  }
  return res;
}

function calculatePacket({type, value, packets}) {
  if (type === 4) {
    return value;
  }

  const subValues = packets?.map(p => calculatePacket(p));

  if (type === 0) {
    return subValues.reduce((a, b) => a + b, 0);
  }

  if (type === 1) {
    return subValues.reduce((a, b) => a * b, 1);
  }

  if (type === 2) {
    return Math.min(...subValues);
  }

  if (type === 3) {
    return Math.max(...subValues);
  }

  if (type === 5) {
    return subValues[0] > subValues[1] ? 1 : 0;
  }

  if (type === 6) {
    return subValues[0] < subValues[1] ? 1 : 0;
  }

  if (type === 7) {
    return subValues[0] === subValues[1] ? 1 : 0;
  }

  throw 'unreachable !!';
}

function solution(data) {
  let [packet] = readPacket(data);
  return sumVersions(packet);
}

function solution2(data) {
  let [packet] = readPacket(data);
  return calculatePacket(packet);
}

(async function() {
  console.log("----(AOC2021 - Day 16)--------------------------[Js]----")

  const exemple = await parseFile('day16.exemple');
  console.log("Exemple :: Part 1 ====>     ", solution(exemple));
  console.log("Exemple :: Part 2 ====>     ", solution2(exemple));
  console.log("--------------------------------------------------------")

  const input = await parseFile('day16.input');
  console.log("Input   :: Part 1 ====>     ", solution(input));
  console.log("Input   :: Part 2 ====>     ", solution2(input));
  console.log("--------------------------------------------------------")
}());
