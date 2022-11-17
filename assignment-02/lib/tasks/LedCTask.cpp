#include <Arduino.h>
#include "LedCTask.h"

LedCTask::LedCTask(int pin, StateTask* stateTask){
  _pin = pin;
  _stateTask = stateTask;
  _led = new Led(_pin);
  _state = OFF;
}
  
void LedCTask::tick(){
  if(_state != OFF && _stateTask->getState() == StateTask::DeviceState::NORMAL){
    _led->switchOff();
    _state = OFF;
  } else if(_stateTask->getState() == StateTask::DeviceState::PREALARM){
    switch(_state){
      case ON:
        _led->switchOff();
        _state = OFF;
        break;
      case OFF:
        _led->switchOn();
        _state = ON;
        break;
    }
    Serial.print("Blinking - ");
  } else if(_state != ON && _stateTask->getState() == StateTask::DeviceState::ALARM){
    _led->switchOn();
    _state = ON;
  }
  Serial.print("State: ");
  Serial.println(_state);
}
