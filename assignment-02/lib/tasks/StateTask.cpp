#include <Arduino.h>
#include <constants.h>
#include "StateTask.h"
  
void StateTask::init(int period){
  Task::init(period);
  _state = DeviceState::NORMAL;
  _sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
}
  
void StateTask::tick(){
  int distance = _sonar->getDistance();
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
