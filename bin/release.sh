#!/bin/bash

set -e

VERSION="$1"
REPOSITORY_ID="$2"
REPOSITORY_URL="$3"

if [ -z "${VERSION}" ] || [  -z "${REPOSITORY_ID}" ] || [ -z "${REPOSITORY_URL}" ]; then
    echo "Usage: release.sh VERSION REPOSITORY_ID REPOSITORY_URL"
    exit 1
fi
echo ""
echo "Starting release '${VERSION}'"
echo ""

echo ""
echo "====================================="
echo "Changing pom.xml versions to ${VERSION}"
echo "====================================="
echo ""

sed -i "s/\(<\!-- version -->\)/<version>${VERSION}<\/version>/" core/core/pom.xml
sed -i "s/\(<\!-- version -->\)/<version>${VERSION}<\/version>/" core/assertions/pom.xml

echo ""
echo "====================================="
echo "Installing core and assertions jars  "
echo "====================================="
echo ""

mvn -f core/core clean install
mvn -f core/assertions clean install

echo ""
echo "====================================="
echo "Deploying core and assertions jars   "
echo "====================================="
echo ""

mvn -N deploy:deploy-file -DgroupId=io.doov \
  -DartifactId=doov-core \
  -Dversion=${VERSION} \
  -Dpackaging=jar \
  -Dfile=core/core/target/doov-core-${VERSION}.jar \
  -DrepositoryId=${REPOSITORY_ID} \
  -Durl=${REPOSITORY_URL} &&
mvn -N deploy:deploy-file -DgroupId=io.doov \
  -DartifactId=doov-core \
  -Dversion=${VERSION} \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=core/core/target/doov-core-${VERSION}-sources.jar \
  -DrepositoryId=${REPOSITORY_ID} \
  -Durl=${REPOSITORY_URL} &&
mvn -N deploy:deploy-file -DgroupId=io.doov \
  -DartifactId=doov-assertions \
  -Dversion=${VERSION} \
  -Dpackaging=jar \
  -Dfile=core/assertions/target/doov-assertions-${VERSION}.jar \
  -DrepositoryId=${REPOSITORY_ID} \
  -Durl=${REPOSITORY_URL} &&
mvn -N deploy:deploy-file -DgroupId=io.doov \
  -DartifactId=doov-assertions \
  -Dversion=${VERSION} \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=core/assertions/target/doov-assertions-${VERSION}-sources.jar \
  -DrepositoryId=${REPOSITORY_ID} \
  -Durl=${REPOSITORY_URL}

echo ""
echo "====================================="
echo "Commiting and tagging versions in git"
echo "====================================="
echo ""

git commit -a -m "Release version ${VERSION}"
git tag ${VERSION}

git revert --no-commit HEAD
git commit -a -m "Revert versions to SNAPSHOT"

echo ""
echo "====================================="
echo "Pushing git commits                  "
echo "====================================="
echo ""

git push
git push --tags

echo ""
echo "Release '${VERSION}' finished"
echo ""
