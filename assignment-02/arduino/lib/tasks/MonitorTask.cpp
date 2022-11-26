#include <Arduino.h>
#include <constants.h>
#include "MonitorTask.h"
#include <MsgService.h>
#include <utils.h>

MonitorTask::MonitorTask() : 
  _sonar(P_SONAR_ECHO, P_SONAR_TRIG)
{
  
}

void MonitorTask::tick() {
    int value = distanceToWaterLevel(_sonar.getDistance());
    float distance = sonarPulseToMeter(value);
    // float t = value / 1000.0 / 1000.0 / 2;
    // float distance = t * SOUND_SPEED;
    // distance *= 100;

    MsgService.sendMsg(String((int)distance));
}

