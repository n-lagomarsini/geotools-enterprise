#!/bin/bash

# error out if any statements fail
set -e

function usage() {
  echo "$0 [options] <tag>"
  echo
  echo " tag : Release tag (eg: 2.7.5, 8.0-RC1, ...)"
  echo
  echo "Options:"
  echo " -h          : Print usage"
  echo " -b <branch> : Branch to release from (eg: master, 8.x, ...)"
  echo
}

# parse options
while getopts "hb:r:u:e:" opt; do
  case $opt in
    h)
      usage
      exit
      ;;
    b)
      branch=$OPTARG
      ;;
    \?)
      usage
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument"
      exit 1
      ;;
  esac 
done

# clear options to parse main arguments
shift $(( OPTIND -1 ))
tag=$1

# sanity check
if [ -z $tag ] || [ ! -z $2 ]; then
  usage
  exit 1
fi

# load properties + functions
. "$( cd "$( dirname "$0" )" && pwd )"/properties
. "$( cd "$( dirname "$0" )" && pwd )"/functions

if [ `is_version_num $tag` == "0" ]; then  
  echo "$tag is a not a valid release tag"
  exit 1
fi
if [ `is_primary_branch_num $tag` == "1" ]; then  
  echo "$tag is a not a valid release tag, can't be same as primary branch name"
  exit 1
fi

echo "Parameters:"
echo "  branch = $branch"
echo "  tag = $tag"

# move to root of repo
pushd ../../ > /dev/null

# check to see if a release branch already exists
set +e && git checkout rel_$tag && set -e
if [ $? == 1 ]; then
  # release branch already exists, kill it
  echo "branch rel_$tag does not exists, ERROR: run build_release.sh before"
  exit 1;
fi

# ensure no changes on it
set +e
git status | grep "working directory clean"
if [ "$?" == "1" ]; then
  echo "branch rel_$tag dirty, exiting"
  exit 1
fi
set -e

# change to release branch
set +e && git checkout rel_$branch && set -e
if [ $? == 1 ]; then
  # release branch does not exists
  echo "branch rel_$branch does not exists, ERROR: run build_release.sh before"
  exit 1;
fi

# merge the tag release branch into main release branch and tag it
# git checkout rel_$branch
git merge -m "Merging rel_$tag into rel_$branch" rel_$tag
git tag $tag

# push them up
git push origin rel_$branch
git push origin $tag

popd > /dev/null

echo "merge completed"
exit 0
