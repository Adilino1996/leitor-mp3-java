#!/usr/bin/env sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
cd "$ROOT_DIR"

rm -rf bin
mkdir -p bin

javac -encoding UTF-8 -d bin \
  src/model/NoMusica.java \
  src/model/PlaylistDupla.java \
  src/model/PlayerService.java \
  src/ui/MenuConsole.java \
  src/ui/LeitorMP3.java

javac -encoding UTF-8 -cp bin -d bin \
  tests/PlaylistTest.java \
  tests/PlayerTest.java \
  tests/TestRunner.java

java -cp bin TestRunner
