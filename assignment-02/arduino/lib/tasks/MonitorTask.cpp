#include <Arduino.h>
#include <constants.h>
#include "MonitorTask.h"
#include <MsgService.h>

MonitorTask::MonitorTask() {
  _sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
}

void MonitorTask::tick() {
    int distance = _sonar->getDistance();
    MsgService.sendMsg(String(distance));
}

