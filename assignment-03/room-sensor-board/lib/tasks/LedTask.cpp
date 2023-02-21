#include <Arduino.h>
#include "LedTask.h"

#define PIN_LED 4
#define PIN_PIR 15

LedTask::LedTask() :
  _led(PIN_LED),
  _pir(PIN_PIR)
{
  // _ledState = OFF;
  _someoneIn = false;
}

  
void LedTask::tick(){
  Serial.println("Tick");
  if (_pir.isTriggered()) {
    _someoneIn = !_someoneIn;

    if (_someoneIn) {
      _led.switchOn();
    } else {
      _led.switchOff();
    }
  }
}
