
# FitGenerator

FitGenerator is a Scala tool that converts HTML tables (from "fit" files) into C++ header and source files. It parses HTML files, extracts table data, and generates corresponding C++ code for use in your projects.

## Features

- Parses HTML files containing tables using the [scala-scraper](https://github.com/ruippeixotog/scala-scraper) library.
- Automatically detects column types (int, double, or string).
- Generates C++ header (`.h`) and source (`.cpp`) files for each table found.
- Command-line interface for easy automation.

## Requirements

- Java 17+
- [sbt](https://www.scala-sbt.org/) (Scala Build Tool)
- Scala 2.12.x

## Installation

Clone the repository and install dependencies using sbt:

```shell
git clone https://github.com/jsonzilla/fit_generator.git
cd fit_generator
sbt update
```

## Building

To build a runnable JAR with all dependencies:

```shell
sbt assembly
```

This will produce `fitGenerator.jar` in the project root.

## Usage

Run the tool with:

```shell
java -jar fitGenerator.jar path/to/your/file.html
```

- The tool will parse the HTML file, extract tables, and generate corresponding `.h` and `.cpp` files in the current directory.
- Each table in the HTML will result in a C++ class named `FitCheck<TableName>`.

## Example

Suppose you have an HTML file with tables. Run:

```shell
java -jar fitGenerator.jar my_tables.html
```

You will get files like `FitCheckMyTable.h` and `FitCheckMyTable.cpp` generated.

## Project Structure

- `src/main/scala/TestsSourceGenerator.scala`: Main entry point; parses arguments and coordinates the generation.
- `src/main/scala/TableParser.scala`: Extracts and parses table data from HTML.
- `src/main/scala/SimpleTable.scala`: Represents a parsed table.
- `src/main/scala/FixtureGenerator.scala`: Generates C++ code from a parsed table.

## Development

- To add new table parsing logic, extend `TableParser`.
- To customize C++ code generation, modify `FixtureGenerator`.

## License

See [LICENSE](LICENSE).

