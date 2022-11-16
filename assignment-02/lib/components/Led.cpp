#include "Led.h"
#include "Arduino.h"

Led::Led(int pin){
  this->_pin = pin;
  pinMode(pin, OUTPUT);
}

void Led::switchOn(){
  digitalWrite(_pin, HIGH);
}

void Led::switchOff(){
  digitalWrite(_pin, LOW);
};
