# gr

GR HW

## Installation

https://github.com/mgarland/gr

## Usage

Runs in one of two modes.  In server mode a jetty web service is started.  Otherwise it runs
like a script and loads a file, sorts it and displays it to the screen.

    $ java -jar gr-0.1.0-standalone.jar [-h --help] for usage
    $ java -jar gr-0.1.0-standalone.jar [-s --server] for server mode
    $ java -jar gr-0.1.0-standalone.jar [-f --file] <file-name> [-o --sort] <1|2|3>

## Options

-h, --help  Dislays usage text and ignores all other args.

-s, --server  Starts a jetty-based web server ignoring file and sort args.

-f, --file <file-name>  Full path name to the file to load when not running in server mode

-o, --sort <sort-option>  Dictates sorting preference.  Display help to see valid options. 

## Examples

java -jar gr-0.1.0-standalone.jar -f /tmp/input.txt -o 1

java -jar gr-0.1.0-standalone.jar --server

## License

Copyright © 2018

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
