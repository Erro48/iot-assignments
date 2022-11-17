#include <Arduino.h>
#include "LedBTask.h"

LedBTask::LedBTask(int pin, StateTask* stateTask){
  _pin = pin;
  _stateTask = stateTask;
  _led = new Led(_pin);
  _state = OFF;
}
  
  
void LedBTask::tick(){
  if(_state != ON && _stateTask->getState() == StateTask::DeviceState::NORMAL){
    _led->switchOn();
    _state = ON;
  } else if(_state != OFF && _stateTask->getState() != StateTask::DeviceState::NORMAL) {
    _led->switchOff();
    _state = OFF;
  }
}
