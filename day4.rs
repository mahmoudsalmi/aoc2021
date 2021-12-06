use std::fs::File;
use std::io::{BufRead, BufReader, Result};

type Matrix = [[i64; 5]; 5];

#[derive(Debug, Clone)]
struct Day4Data {
    tokens: Vec<i64>,
    matrices: Vec<Matrix>,
}

fn parse_file(filename: &str) -> Result<Day4Data> {
    let mut reader = BufReader::new(
        File::open(filename)?
    );

    let mut line = String::new();

    reader.read_line(&mut line)?;

    let tokens: Vec<i64> = line
        .replace(',', " ")
        .split_whitespace()
        .map(|x| x.to_string().parse::<i64>().unwrap_or(0))
        .collect();


    let mut matrices: Vec<Matrix> = Vec::new();

    line.clear();
    while reader.read_line(&mut line).unwrap() > 0 {
        let mut matrix: Matrix = [[0; 5]; 5];
        for i in 0..5 {
            reader.read_line(&mut line)?;
            let line_values: Vec<i64> = line.trim().split_whitespace().map(|x| x.to_string().parse::<i64>().unwrap_or(99999)).collect();
            line.clear();
            for j in 0..5 {
                matrix[i][j] = line_values[j];
            }
        }
        matrices.push(matrix);
    }

    Ok(Day4Data { tokens, matrices })
}

fn apply_token(mut matrices: Vec<Matrix>, token: i64) -> Vec<Matrix> {
    for m in matrices.iter_mut() {
        for r in 0..5 {
            for c in 0..5 {
                if m[r][c] == token {
                    m[r][c] = -1;
                }
            }
        }
    }

    matrices
}

fn matrix_sum(m: Matrix) -> i64 {
    let mut res = 0;
    for r in 0..5 {
        for c in 0..5 {
            if m[r][c] > 0 {
                res += m[r][c];
            }
        }
    }
    res
}

fn matrix_resolved(m: Matrix) -> bool {
    for r in 0..5 {
        if m[r].iter().sum::<i64>() == -5 {
            return true;
        }
    }
    for c in 0..5 {
        let mut solved = true;
        for r in 0..5 {
            if m[r][c] != -1 {
                solved = false;
            }
        }
        if solved {
            return true;
        }
    }
    false
}

fn part1(data: Day4Data) -> i64 {
    let mut matrices = data.matrices;

    let mut solution: i64 = 0;
    for t in data.tokens.iter() {
        matrices = apply_token(matrices, *t);

        let mut solved = false;

        for m in matrices.iter() {
            if matrix_resolved(*m) {
                solution = t * matrix_sum(*m);
                solved = true;
                break;
            }
        }
        if solved {
            break;
        }
    }
    solution
}

fn part2(data: Day4Data) -> i64 {
    let mut matrices = data.matrices;

    let mut solved_indexes: Vec<usize> = Vec::new();
    let mut solution: i64 = 0;
    for t in data.tokens.iter() {
        matrices = apply_token(matrices, *t);

        let mut solved = true;

        for (idx, m) in matrices.iter().enumerate() {
            if !matrix_resolved(*m) {
                solved = false;
            } else if !solved_indexes.contains(&idx) {
                solved_indexes.push(idx);
            }
        }
        if solved {
            let last: usize = *solved_indexes.last().unwrap();
            solution = t * matrix_sum(matrices[last]);
            break;
        }
    }
    solution
}

fn main() -> Result<()> {
    println!("----(AOC2021 - Day 04)------------------------[Rust]----");

    let exemple = parse_file("day4.exemple")?;
    println!("Exemple :: Part 1 ====>     {}", part1(exemple.clone()));
    println!("Exemple :: Part 2 ====>     {}", part2(exemple));
    println!("--------------------------------------------------------");

    let input = parse_file("day4.input")?;
    println!("Input   :: Part 1 ====>     {}", part1(input.clone()));
    println!("Input   :: Part 2 ====>     {}", part2(input));
    println!("--------------------------------------------------------");

    Ok(())
}
