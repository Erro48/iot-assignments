#include <Arduino.h>
#include "Led.h"

Led::Led(int pin) {
  _pin = pin;
  pinMode(pin, OUTPUT);
}

void Led::switchOn(){
  digitalWrite(_pin, HIGH);
}

void Led::switchOff(){
  digitalWrite(_pin, LOW);
};
