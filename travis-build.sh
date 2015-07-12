#!/bin/bash
set -e
rm -rf *.zip
./gradlew clean check assemble

filename=$(find build/libs -name "*.jar" | head -1)
filename=$(basename "$filename")

EXIT_STATUS=0
echo "Publishing archives for branch $TRAVIS_BRANCH"
if [[ -n $TRAVIS_TAG ]] || [[ $TRAVIS_BRANCH == '2.x' && $TRAVIS_PULL_REQUEST == 'false' ]]; then

  echo "Publishing archives"

  if [[ -n $TRAVIS_TAG ]]; then
      ./gradlew bintrayUpload || EXIT_STATUS=$?
  else
      ./gradlew publish || EXIT_STATUS=$?
  fi

fi

exit $EXIT_STATUS
