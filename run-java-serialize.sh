#!/bin/bash
testfilename=$1
echo Writing sample map to $testfilename
java -cp ./target/monster-map-1.0-SNAPSHOT.jar questions.SimpleSerializationWrite $testfilename
ls -l ${testfilename}
echo ======================================================================================
echo Reading sample map from $testfilename
java -cp ./target/monster-map-1.0-SNAPSHOT.jar questions.SimpleSerializationRead $testfilename
ls -l ${testfilename}
echo ======================================================================================
echo Done.