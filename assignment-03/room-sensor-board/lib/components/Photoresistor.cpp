#include <Arduino.h>
#include "Photoresistor.h"

Photoresistor::Photoresistor(int pin){
  _pin = pin;
  pinMode(_pin, INPUT);
}

int Photoresistor::getLuminosity(){
  return analogRead(_pin);
}
