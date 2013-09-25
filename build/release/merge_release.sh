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
  echo " -r <rev>    : Revision to release (eg: a1b2kc4...)"
  echo " -u <user>   : git user"
  echo " -e <passwd> : git email"
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
    r)
      rev=$OPTARG
      ;;
    u)
      git_user=$OPTARG
      ;;
    e)
      git_email=$OPTARG
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
echo "  revision = $rev"
echo "  tag = $tag"

# move to root of repo
pushd ../../ > /dev/null

# clear out any changes
git reset --hard HEAD

# check to see if a release branch already exists
set +e && git checkout rel_$tag && set -e
if [ $? == 1 ]; then
  # release branch already exists, kill it
  echo "branch rel_$tag does not exists, ERROR: run build_release.sh before"
  exit 1;
fi

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
