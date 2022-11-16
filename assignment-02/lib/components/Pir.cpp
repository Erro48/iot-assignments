#include <Arduino.h>
#include "Pir.h"

Pir::Pir(int pin){
  _pin = pin;
  pinMode(_pin, INPUT);
}

int Pir::isTriggered(){
  return digitalRead(_pin);
}
