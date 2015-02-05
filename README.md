# StringsParser
This is a preliminary work for using the [strings](http://en.wikipedia.org/wiki/Strings_(Unix)) (or strings-alternative) command in [Apache Tika](http://tika.apache.org).

## Getting started
This repository includes the implementation of the StringsParser, a parser that uses the strings command in order to extract ASCII strings from binary files.

Use build.sh and run.sh scripts (under Unix-like OS) for compiling and testing StringsParser.

## Notes
The repository is organized as follows:
  govdocs1/016
  This folder contains some files from [govdocs1](http://digitalcorpora.org/corpora/govdocs) (#016 subset) that Tika is not able to detect. These files are marked as application/octet-stream.
  
  - src
  
  This folder includes .java source files. In addition to StringsParser.java and StringsConfig.java (the main classes), there is also a extremely simple StringsTest.java for launching the parser against a single file. The latter source file uses a simple utility called Timer.java.
  
  - README.md
  
  This README file.
  
  - build.sh
  
  This scripts compiles the .java source code using the javac command.
  
  - run.sh
  
  This scripts runs the StringsParser against the files in the govdocs1/016 folder.

##License
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
