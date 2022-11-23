#include <Arduino.h>
#include <constants.h>
#include "LedCTask.h"

LedCTask::LedCTask(StateTask* stateTask) :
  _led(P_LED_C)
{
  _stateTask = stateTask;
  _state = OFF;
}
  
void LedCTask::tick(){
  if(_state != OFF && _stateTask->getState() == StateTask::DeviceState::NORMAL){
    _led.switchOff();
    _state = OFF;
  } else if(_stateTask->getState() == StateTask::DeviceState::PREALARM){
    switch(_state){
      case ON:
        _led.switchOff();
        _state = OFF;
        break;
      case OFF:
        _led.switchOn();
        _state = ON;
        break;
    }
  } else if(_state != ON && _stateTask->getState() == StateTask::DeviceState::ALARM){
    _led.switchOn();
    _state = ON;
  }
}
