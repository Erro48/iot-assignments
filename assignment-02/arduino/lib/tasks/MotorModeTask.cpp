#include <Arduino.h>
#include <constants.h>
#include "MotorModeTask.h"
#include  <MsgService.h>

MotorModeTask::MotorModeTask(StateTask* stateTask) :
    _button(P_BUTTON)
{
  _stateTask = stateTask;
  _motorMode = MotorMode::AUTO;
  _currentBtnState = LOW;
  _lastBtnState = LOW;
}
  
void MotorModeTask::tick(){
    _currentBtnState = _button.isPressed();


    /* check message command */
    if (MsgService.isMsgAvailable()) {
        Msg* msg = MsgService.receiveMsg();
        
        if (msg->getContent() == "false") {
            _motorMode = MotorMode::AUTO;
        } else if (msg->getContent() == "true" && _stateTask->getState() == StateTask::DeviceState::ALARM) {
            _motorMode = MotorMode::CONSOLE;
        } else {
            MsgService.sendMsg("*[01]Cannot switch to manual mode");
        }   
        
        delete msg;
    }

    if (_stateTask->getState() == StateTask::DeviceState::ALARM) {
        if (_currentBtnState == LOW && _lastBtnState == HIGH) {
            /* on button release */
            if (_motorMode != MotorMode::MANUAL) _motorMode = MotorMode::MANUAL;
            else _motorMode = MotorMode::AUTO;
        }
        _lastBtnState = _currentBtnState;

    } else if (_stateTask->getState() != StateTask::DeviceState::ALARM && _motorMode != MotorMode::AUTO) {
        _motorMode = MotorMode::AUTO;
    }
}

MotorModeTask::MotorMode MotorModeTask::getMotorMode() {
    return _motorMode;
}