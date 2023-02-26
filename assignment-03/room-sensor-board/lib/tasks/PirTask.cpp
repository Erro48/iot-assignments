#include <Arduino.h>
#include "PirTask.h"

#define PIN_LED 4
#define PIN_PIR 15

PirTask::PirTask(bool *someoneIn) :
  _led(PIN_LED),
  _pir(PIN_PIR)
{
  _someoneIn = someoneIn;
  _lastTriggerStatus = false;
  _lastTrigger = 0;
}

  
void PirTask::tick(){
  bool trigger = _pir.isTriggered();

  if (trigger && _lastTriggerStatus != trigger) {
    *_someoneIn = !*_someoneIn;

    if (*_someoneIn) {
      _led.switchOn();
    } else {
      _led.switchOff();
    }
  }

  _lastTriggerStatus = trigger;
}
