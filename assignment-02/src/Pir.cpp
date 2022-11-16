#include <Arduino.h>
#include "Pir.h"

Pir::Pir(int pin){
  this->_pin = pin;
  pinMode(_pin, INPUT);
}

int Pir::isTriggered(){
  return digitalRead(this->_pin);
}
