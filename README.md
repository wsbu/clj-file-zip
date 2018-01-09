# clj-file-zip

A simple Clojure library for dealing with zip files

## Usage
[![Clojars Project](https://img.shields.io/clojars/v/clj-file-zip.svg)](https://clojars.org/clj-file-zip)

## Example

### Assuming you're in a repl (`lein repl`)...

Import the core namespace:

`(use 'clj-file-zip.core)`

Zip a single file:

`(zip "/tmp/foo" "/tmp/foo.zip")`

Zip a folder recursively:

`(zip "/tmp/folder" "/tmp/folder.zip")`

Unzip an archive to destination folder:

`(unzip "/tmp/folder.zip" "/tmp/")`

Create an archive and add files with no hierarchy

`(zip-files ["filename1" "filename2"] "/tmp/archive.zip")`
