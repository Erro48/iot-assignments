#include <Arduino.h>
#include "StateTask.h"

#define SONAR_ECHO 8
#define SONAR_TRIG 7
#define WL1 500
#define WL2 800

  
void StateTask::init(int period){
  Task::init(period);
  this->_state = 0;
  this->_sonar = new Sonar(SONAR_ECHO, SONAR_TRIG);
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
