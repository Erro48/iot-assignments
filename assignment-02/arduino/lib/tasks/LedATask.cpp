#include <Arduino.h>
#include <constants.h>
#include "LedATask.h"

LedATask::LedATask(int pin, StateTask* stateTask) :
  _led(pin),
  _pir(P_PIR),
  _photoresistor(P_PHOTORESISTOR)
{
  _pin = pin;
  _stateTask = stateTask;
  _state = OFF;
}

  
void LedATask::tick(){
  _pir.updateLastTrigger();
  
  if(_stateTask->getState() != StateTask::DeviceState::ALARM){
    if(_state != ON && _pir.isTriggered() && _photoresistor.getLuminosity() < MIN_LUM){
      _led.switchOn();
      _state = ON;
    }
    if(_state != OFF && (millis() - _pir.getLastTrigger() > T1 || _photoresistor.getLuminosity() >= MIN_LUM)){
      _led.switchOff();
      _state = OFF;
    }
  } else {
    if(_state == ON){
      _led.switchOff();
    }
  }
}
