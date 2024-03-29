#include <Arduino.h>
#include <constants.h>
#include "MotorTask.h"
#include <MsgService.h>

MotorTask::MotorTask(StateTask* stateTask, MotorModeTask* motorModeTask) :
    _sonar(P_SONAR_ECHO, P_SONAR_TRIG),
    _pot(P_POT)
{
    pinMode(P_MOTOR, OUTPUT);
    _stateTask = stateTask;
    _motorModeTask = motorModeTask;
    _motorMode = MotorModeTask::MotorMode::AUTO;

    _alpha = MOTOR_MIN_ALPHA;
    _lastDistance = _sonar.getDistance();
    _servo.attach(P_MOTOR);
}

void MotorTask::tick() {
    _motorMode = _motorModeTask->getMotorMode();
    
    switch (_motorMode)
    {
        case MotorModeTask::MotorMode::AUTO:
        autoMode();
        break;

        case MotorModeTask::MotorMode::MANUAL:
        manualMode();
        break;
        
        case MotorModeTask::MotorMode::CONSOLE:
        consoleMode();
        break;
    }

    _servo.write(_alpha);
    
}

int MotorTask::getAlpha() {
    return _alpha;
}

void MotorTask::autoMode() {
    int distance = _sonar.getDistance();

    if (_stateTask->getState() == StateTask::DeviceState::ALARM) {
        if (_lastDistance != distance && distance >= WLMAX && abs(_lastDistance - distance) > MIN_MOTOR_MV) {
            if (distance < WLMAX) {
                distance = WLMAX;
            }

            _alpha = map(distance, WL2, WLMAX, MOTOR_MIN_ALPHA, MOTOR_MAX_ALPHA);
            if(_alpha < 0){
                _alpha = 0;
            }

            _lastDistance = distance;
        }
    }
}

void MotorTask::manualMode() {
    _alpha = map(_pot.read(), POT_MIN_VALUE, POT_MAX_VALUE, MOTOR_MIN_ALPHA, MOTOR_MAX_ALPHA);
}

void MotorTask::consoleMode() {
    if (MsgService.isMsgAvailable()) {
        Msg* msg = MsgService.receiveMsg();    
        int alpha = msg->getContent().toInt();
        _alpha = alpha;
        delete msg;
    }
}