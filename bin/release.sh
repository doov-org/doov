#!/bin/bash

set -e

VERSION="$1"
REPOSITORY_ID="$2"
REPOSITORY_URL="$3"
GPG_KEYNAME="$4"

if [ -z "${VERSION}" ] || [  -z "${REPOSITORY_ID}" ] || [ -z "${REPOSITORY_URL}" ] ; then
    echo "Usage: release.sh VERSION REPOSITORY_ID REPOSITORY_URL [GPG_KEYNAME]"
    exit 1
fi
echo ""
echo "Starting release '${VERSION}'"
echo ""

echo ""
echo "====================================="
echo "Testing code                         "
echo "====================================="
echo ""

mvn clean test

echo ""
echo "====================================="
echo "Changing pom.xml versions to ${VERSION}"
echo "====================================="
echo ""

sed -i "s/\(<version>1.0-SNAPSHOT<\/version>\)/<version>${VERSION}<\/version>/" pom.xml
sed -i "s/\(<version>1.0-SNAPSHOT<\/version>\)/<version>${VERSION}<\/version>/" core/pom.xml
sed -i "s/\(<version>1.0-SNAPSHOT<\/version>\)/<version>${VERSION}<\/version>/" assertions/pom.xml
sed -i "s/\(<version>1.0-SNAPSHOT<\/version>\)/<version>${VERSION}<\/version>/" generator/pom.xml
echo "Version changed to ${VERSION} in pom.xml, core/pom.xml, assertions/pom.xml, generator/pom.xml"

echo ""
echo "====================================="
echo "Deploying parent pom                 "
echo "====================================="
echo ""

if [ -z "${GPG_KEYNAME}" ] ; then
  mvn -N -P release -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
else
  mvn -D gpg.keyname="${GPG_KEYNAME}" -N -P release -P sign -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
fi

echo ""
echo "====================================="
echo "Deploying core, assertions, generator"
echo "====================================="
echo ""

if [ -z "${GPG_KEYNAME}" ] ; then
  mvn -f core/pom.xml -P release -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
  mvn -f assertions/pom.xml -P release -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
  mvn -f generator/pom.xml -P release -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
else
  mvn -D gpg.keyname="${GPG_KEYNAME}" -f core/pom.xml -P release -P sign -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
  mvn -D gpg.keyname="${GPG_KEYNAME}" -f assertions/pom.xml -P release -P sign -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
  mvn -D gpg.keyname="${GPG_KEYNAME}" -f generator/pom.xml -P release -P sign -D altDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
fi

echo ""
echo "====================================="
echo "Commiting and tagging versions in git"
echo "====================================="
echo ""

git commit -a -m "[release] Release version ${VERSION}"
git tag ${VERSION}

echo ""
echo "====================================="
echo "Releasing maven site                 "
echo "====================================="
echo ""

mvn -pl core -P publish-site clean site
mvn -pl assertions -P publish-site clean site
mvn -pl generator -P publish-site clean site
git add .
git commit -m "[release] Release maven site ${VERSION}"

echo ""
echo "====================================="
echo "Reverting versions to SNAPSHOT       "
echo "====================================="
echo ""

git revert --no-commit HEAD~1
git commit -a -m "[release] Revert versions to SNAPSHOT"

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
