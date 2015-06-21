@echo off
echo Writing sample map to %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.OptimizedSerializationWrite %1
echo ======================================================================================
echo Reading sample map from %1
java -cp .\target\monster-map-1.0-SNAPSHOT.jar questions.OptimizedSerializationRead %1
echo ======================================================================================
echo Done.