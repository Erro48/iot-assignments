#include <Arduino.h>
#include "Potentiometer.h"

Potentiometer::Potentiometer(int pin){
  _pin = pin;
  pinMode(pin, INPUT);
}

int Potentiometer::read(){
  
  return analogRead(_pin);
};
