#include <Arduino.h>
#include <constants.h>
#include "StateTask.h"
  
void StateTask::init(int period){
  Task::init(period);
  this->_state = 0;
  this->_sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
}
  
void StateTask::tick(){
  int distance = _sonar->getDistance();
  if(distance < WL1){
    this->_state = 0;
  } else if (distance >= WL1 && distance < WL2){
    this->_state = 1;
  } else {
    this->_state = 2;
  }
}

int StateTask::getState() {
  return this->_state;
}
