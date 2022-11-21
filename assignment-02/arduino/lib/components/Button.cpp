#include <Arduino.h>
#include <constants.h>
#include "Button.h"

Button::Button(int pin){
  _pin = pin;
  _state = LOW;
  _lastTimePressed = 0;
  pinMode(pin, INPUT);
}

bool Button::isPressed(){
  if (millis() - _lastTimePressed > DEBOUNCE_TIME) {
    _state = digitalRead(_pin);
    _lastTimePressed = millis();
  }
  
  return _state;
};
