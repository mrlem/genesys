# Genesys

*A simple family tree generator that scales.*

## Features

It supports:
* GEDCOM to various visualization formats (PDF & SVG among others)
* a single tree format, suitable for large trees, that doesn't become a mess when the tree contains loops

Here's what it looks like for a large tree:

![Global family tree](screenshots/global_tree.png)

And looking closer:

![Family tree detail](screenshots/tree_detail.png)

## Usage

You can use the [latest release](https://github.com/mrlem/genesys/releases/latest) on github.
For detailed step-by-step instructions, see the [Quickstart](doc/QUICKSTART.md).

Full command line documentation:
```
Usage: genesys options_list
Arguments:
    GEDCOM file (optional) { String }
Options:
    --output, -o [<GEDCOM file>.pdf] -> Output file { String }
    --outputType, -t [PDF]           -> Output file type { Value should be one of [fig, jpeg, pdf, png, svg, webp] }
    --root, -r [<most recent>]       -> Individual, like "John Henry Doe", or identifier,  like "I1" { String }
    --noPreview, -np [false]         -> No preview
    --help, -h                       -> Usage info
```


*Note: Java Runtime environment and GraphViz `dot` utility are needed to run Genesys.*

Or if you're a developer and want to dive in the app: `./gradlew run`.

## About

### Author

*Sébastien Guillemin*

### Motivation

I just couldn't find a free tool to create a tree without limitations in terms of number of generations, that still
looked decent for a large tree (500+ persons), and that handled family loops like I wanted.

Also, I was tired of
importing my GEDCOM file in every tool on the planet, when all I wanted was to create a PDF.

### Acknowlegments

Built with:

* [gedcom5-java](https://github.com/FamilySearch/gedcom5-java) parser, from [FamilySearch](https://github.com/FamilySearch/).
* [GraphViz](https://graphviz.org/) graph generator (not included)

Also thanks to:

* [GraphvizOnline](https://dreampuf.github.io/GraphvizOnline/), a wonderful tool for realtime experiments, that I used
extensively to adjust my GraphViz layout.
