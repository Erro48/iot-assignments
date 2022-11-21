#include <Arduino.h>
#include <constants.h>
#include "StateTask.h"
  
StateTask::StateTask() : 
  _sonar(P_SONAR_ECHO, P_SONAR_TRIG)
{
  _state = DeviceState::NORMAL;
}

void StateTask::tick() {
  // Serial.print("STATE: ");
  // Serial.println(_state);
  int distance = _sonar.getDistance();
  if(distance < WL1){
    _state = DeviceState::NORMAL;
  } else if (distance >= WL1 && distance < WL2){
    _state = DeviceState::PREALARM;
  } else {
    _state = DeviceState::ALARM;
  }
}

StateTask::DeviceState StateTask::getState() {
  return _state;
}
