#include <Arduino.h>
#include <constants.h>
#include "MotorTask.h"

MotorTask::MotorTask(int pin) {
    _pin = pin;
    _alpha = 0;
    _sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
    _lastDistance = _sonar->getDistance();
    _servo.attach(_pin);
}

void MotorTask::tick() {
    int distance = _sonar->getDistance();
    if (_lastDistance != distance) {
        _alpha = map(distance, WL1, WL2, 0, 180);
        _servo.write(_alpha);

        _lastDistance = distance;
    }
}