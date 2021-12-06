use std::fs::File;
use std::io::{BufRead, BufReader, Result};

fn parse_file(filename: &str) -> Result<()> {
    let mut reader = BufReader::new(
        File::open(filename)?
    );

    let mut line = String::new();

     reader.read_line(&mut line)?;
    
    let game: Vec<i32> = line
        .split(',')
        .map(|x| x.parse::<i32>().unwrap())
        .collect();

    println!("game : {:?}", game);

    Ok(())
}

fn main() -> Result<()> {
    //parse_file("day4.exemple")?;
    let a = "123".to_string().parse::<i32>().unwrap();
    println!("{}", a);
    Ok(())
}
