#include <Arduino.h>
#include "LedBTask.h"

LedBTask::LedBTask(int pin, StateTask* stateTask){
  this->_pin = pin;
  this->_stateTask = stateTask;
}
  
void LedBTask::init(int period){
  Task::init(period);
  this->_led = new Led(_pin);
  this->_state = OFF;
}
  
void LedBTask::tick(){
  if(this->_state != ON && this->_stateTask->getState() == 0){
    this->_led->switchOn();
    this->_state = ON;
  } else if(this->_state != OFF && this->_stateTask->getState() != 0) {
    this->_led->switchOff();
    this->_state = OFF;
  }
}
