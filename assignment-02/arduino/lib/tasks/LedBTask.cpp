#include <Arduino.h>
#include "LedBTask.h"

LedBTask::LedBTask(int pin, StateTask* stateTask) :
  _led(pin)
{
  _pin = pin;
  _stateTask = stateTask;
  _state = OFF;
}

  
  
void LedBTask::tick(){
  if(_state != ON && _stateTask->getState() == StateTask::DeviceState::NORMAL){
    _led.switchOn();
    _state = ON;
  } else if(_state != OFF && _stateTask->getState() != StateTask::DeviceState::NORMAL) {
    _led.switchOff();
    _state = OFF;
  }
}
