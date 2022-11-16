#include <Arduino.h>
#include <constants.h>
#include "StateTask.h"
  
void StateTask::init(int period){
  Task::init(period);
  this->_state = DeviceState::NORMAL;
  this->_sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
}
  
void StateTask::tick(){
  int distance = _sonar->getDistance();
  if(distance < WL1){
    this->_state = DeviceState::NORMAL;
  } else if (distance >= WL1 && distance < WL2){
    this->_state = DeviceState::PREALARM;
  } else {
    this->_state = DeviceState::ALARM;
  }
}

StateTask::DeviceState StateTask::getState() {
  return this->_state;
}
