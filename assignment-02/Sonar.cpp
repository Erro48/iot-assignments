#include <Arduino.h>
#include "Sonar.h"

#define TRIG_DELAY 5

Sonar::Sonar(int echoPin, int trigPin){
  this->_echoPin = echoPin;
  this->_trigPin = trigPin;
  /*pinMode(_echoPin, INPUT);
  pinMode(_trigPin, OUTPUT);*/
}

int Sonar::getDistance(){
  digitalWrite(_trigPin, LOW);
  delayMicroseconds(TRIG_DELAY);
  digitalWrite(_trigPin, LOW);
  delayMicroseconds(TRIG_DELAY);
  digitalWrite(_trigPin, LOW);
  delayMicroseconds(TRIG_DELAY);
  return pulseIn(_echoPin, HIGH);
}
