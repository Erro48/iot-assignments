#include <Arduino.h>
#include "Pir.h"

Pir::Pir(int pin){
  _pin = pin;
  _lastTrigger = 0;
  pinMode(_pin, INPUT);
}

int Pir::isTriggered(){
  return digitalRead(_pin);
}

void Pir::updateLastTrigger() {
  if (Pir::isTriggered()) {
    _lastTrigger = millis();
  }
}

unsigned long Pir::getLastTrigger() {
  return _lastTrigger;
}
