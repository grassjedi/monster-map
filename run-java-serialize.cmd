@echo off
echo Writing sample map to %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.SimpleSerializationWrite %1
echo ======================================================================================
echo Reading sample map from %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.SimpleSerializationRead %1
echo ======================================================================================
echo Done.