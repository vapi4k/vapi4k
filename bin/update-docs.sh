#!/bin/bash

git checkout docs
git merge master
git push origin docs
git checkout master
