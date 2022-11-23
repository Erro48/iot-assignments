#include <Arduino.h>
#include <constants.h>
#include "LedBTask.h"

LedBTask::LedBTask(StateTask* stateTask) :
  _led(P_LED_B)
{
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
