#!/bin/bash

BUILD_DIR=/home/ubuntu/build
DEPLOY_DIR=/home/ubuntu
LOG_FILE=$DEPLOY_DIR/app.log

echo "> Build 파일 복사"
BUILD_PATH=$(ls $BUILD_DIR/*.jar | head -n 1)
JAR_NAME=$(basename $BUILD_PATH)
cp $BUILD_PATH $DEPLOY_DIR

echo "> 실행 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> 실행 중인 애플리케이션 없음"
  sleep 1
else
  echo "> 실행 중인 애플리케이션 종료: $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 실행: $JAR_NAME"
nohup java -jar $DEPLOY_DIR/$JAR_NAME > $LOG_FILE 2>&1 &

echo "> 배포 완료"
