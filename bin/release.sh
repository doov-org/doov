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

mvn -N -DaltDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy

echo ""
echo "====================================="
echo "Deploying core, assertions, generator"
echo "====================================="
echo ""

mvn -f core -DaltDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
mvn -f assertions -DaltDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy
mvn -f generator -DaltDeploymentRepository="${REPOSITORY_ID}::default::${REPOSITORY_URL}" clean deploy

echo ""
echo "====================================="
echo "Commiting and tagging versions in git"
echo "====================================="
echo ""

git commit -a -m "Release version ${VERSION}"
git tag ${VERSION}

echo ""
echo "====================================="
echo "Releasing maven site                 "
echo "====================================="
echo ""

mvn -pl core -P publish-site clean site
git commit -a -m "Release maven site ${VERSION}"

echo ""
echo "====================================="
echo "Reverting versions to SNAPSHOT       "
echo "====================================="
echo ""

git revert --no-commit HEAD~1
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
