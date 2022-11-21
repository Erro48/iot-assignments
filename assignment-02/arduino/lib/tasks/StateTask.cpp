#include <Arduino.h>
#include <constants.h>
#include "StateTask.h"
  
StateTask::StateTask() : 
  _sonar(P_SONAR_ECHO, P_SONAR_TRIG)
{
  _state = DeviceState::NORMAL;
}

void StateTask::tick() {
  int distance = _sonar.getDistance();
  if(distance > WL1){
    _state = DeviceState::NORMAL;
    updatePeriod(NORMAL_SAMPLING_PERIOD);
  } else if (distance <= WL1 && distance > WL2){
    _state = DeviceState::PREALARM;
    updatePeriod(PREALARM_SAMPLING_PERIOD);
  } else {
    _state = DeviceState::ALARM;
    updatePeriod(ALARM_SAMPLING_PERIOD);
  }
}

StateTask::DeviceState StateTask::getState() {
  return _state;
}
