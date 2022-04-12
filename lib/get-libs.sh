#!/bin/sh

if [ "${PWD##*/}" = "lib" ];
then
  cd ..
fi
mvn dependency:copy-dependencies
