#include <Arduino.h>
#include <constants.h>
#include "MotorModeTask.h"

MotorModeTask::MotorModeTask(StateTask* stateTask){
  _stateTask = stateTask;
  _button = new Button(P_BUTTON);
  _motorMode = MotorMode::AUTO;
  _currentBtnState = LOW;
  _lastBtnState = LOW;
}
  
void MotorModeTask::tick(){
    _currentBtnState = _button->isPressed();

    if (_stateTask->getState() == StateTask::DeviceState::ALARM) {
        if (_currentBtnState == LOW && _lastBtnState == HIGH) {
            /* on button release */
            if (_motorMode == MotorMode::AUTO) _motorMode = MotorMode::MANUAL;
            else _motorMode = MotorMode::AUTO;
        }
        _lastBtnState = _currentBtnState;
    } 
}

MotorModeTask::MotorMode MotorModeTask::getMotorMode() {
    return _motorMode;
}