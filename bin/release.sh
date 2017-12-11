#!/bin/bash

set -e

VERSION="$1"

if [ -z "$VERSION" ]
then
    echo "Usage: release.sh VERSION"
    exit 1
fi

echo "Releasing $VERSION"

sed -i "s/\(<\!-- version -->\)/<version>${VERSION}<\/version>/" core/core/pom.xml
sed -i "s/\(<\!-- version -->\)/<version>${VERSION}<\/version>/" core/assertions/pom.xml

mvn -f core/core clean install
mvn -f core/assertions clean install

git commit -a -m "Release version $VERSION"
git tag $VERSION

git revert --no-commit HEAD
git commit -a -m "Revert versions to SNAPSHOT"

echo "-------------------------------------"
echo "Release '$VERSION' finished you need to"
echo "- Publish maven artifacts to repository"
echo "- Push new commits with 'git push && git push --tags'"
