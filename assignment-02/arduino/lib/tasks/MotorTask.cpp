#include <Arduino.h>
#include <constants.h>
#include "MotorTask.h"

MotorTask::MotorTask(int pin, StateTask* stateTask) {
    _pin = pin;
    pinMode(_pin, OUTPUT);
    _stateTask = stateTask;
    _alpha = 0;
    _sonar = new Sonar(P_SONAR_ECHO, P_SONAR_TRIG);
    _lastDistance = _sonar->getDistance();
    _temp = 0;
    _servo.attach(_pin);
}

void MotorTask::tick() {
    /*int distance = _sonar->getDistance();

    if (_stateTask->getState() == StateTask::DeviceState::ALARM) {
        if (_lastDistance != distance) {
            _alpha = map(distance, WL1, WL2, 0, 180);
            _servo.write(_alpha);

            Serial.println(_alpha);

            _lastDistance = distance;
        }
    }*/
    Serial.println("HJC");

    // digitalWrite(_pin, HIGH);
    // delayMicroseconds(250);
    // digitalWrite(_pin,LOW);

}

/*

tmax => WL2 - WL1 => dmax

tx : dx = x : d
x = (tx * d) / dx

*/