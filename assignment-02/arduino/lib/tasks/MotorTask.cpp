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
    _servo.attach(_pin);
}

void MotorTask::tick() {
    int distance = _sonar->getDistance();

    if (_stateTask->getState() == StateTask::DeviceState::ALARM) {
        if (_lastDistance != distance && distance <= WLMAX && abs(_lastDistance - distance) > MIN_MV) {
            if (distance > WLMAX) {
                distance = WLMAX;
            }
            Serial.print("Distance: ");
            Serial.println(distance);

            _alpha = map(distance, WL2, WLMAX, 0, 180);

            Serial.print("_Alpha: ");
            Serial.println(_alpha);
            Serial.println("-------------");
            
            _servo.write(_alpha);


            _lastDistance = distance;
        }
    }
    // Serial.println("HJC");

    // digitalWrite(_pin, HIGH);
    // delayMicroseconds(250);
    // digitalWrite(_pin,LOW);

}