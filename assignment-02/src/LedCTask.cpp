#include <Arduino.h>
#include "LedCTask.h"

LedCTask::LedCTask(int pin, StateTask* stateTask){
  this->_pin = pin;
  this->_stateTask = stateTask;
}
  
void LedCTask::init(int period){
  Task::init(period);
  this->_led = new Led(_pin);
  this->_state = OFF;
}
  
void LedCTask::tick(){
  if(this->_state != OFF && this->_stateTask->getState() == 0){
    this->_led->switchOff();
    this->_state = OFF;
  } else if(this->_stateTask->getState() == 1){
    switch(this->_state){
      case ON:
        this->_led->switchOff();
        this->_state = OFF;
        break;
      case OFF:
        this->_led->switchOn();
        this->_state = ON;
        break;
    }
    Serial.print("Blinking - ");
  } else if(this->_state != ON && this->_stateTask->getState() == 2){
    this->_led->switchOn();
    this->_state = ON;
  }
  Serial.print("State: ");
  Serial.println(this->_state);
}
