#include <Arduino.h>
#include "Photoresistor.h"

Photoresistor::Photoresistor(int pin){
  this->_pin = pin;
  pinMode(_pin, INPUT);
}

int Photoresistor::getLuminosity(){
  return analogRead(this->_pin);
}
