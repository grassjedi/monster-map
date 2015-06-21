@echo off
echo Writing sample map to %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.SerializationWrite %1
echo ======================================================================================
echo Reading sample map from %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.SerializationRead %1
echo ======================================================================================
echo Done.