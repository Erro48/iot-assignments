#include <Arduino.h>
#include <constants.h>
#include "LcdDisplayTask.h"

LcdDisplayTask::LcdDisplayTask(StateTask* stateTask, MotorTask* motorTask) :
    _lcd(LCD_ADDRESS, LCD_ROWS, LCD_COLS),
    _sonar(P_SONAR_ECHO, P_SONAR_TRIG)
{
    _stateTask = stateTask;
    _motorTask = motorTask;
}

void LcdDisplayTask::tick(){

    switch(_stateTask->getState()) {
        case StateTask::DeviceState::NORMAL:
        _lcd.clearDisplay();
        _lcd.switchOn(false);
        break;

        case StateTask::DeviceState::PREALARM:
        if (_lcd.isSwitchedOff()) _lcd.switchOn(true);
        _lcd.clearDisplay();
        _lcd.displayWaterLevel(_sonar.getDistance());
        break;

        case StateTask::DeviceState::ALARM:
        if (_lcd.isSwitchedOff()) _lcd.switchOn(true);
        _lcd.clearDisplay();
        _lcd.displayWaterLevel(_sonar.getDistance());
        _lcd.displayMotorAlpha(_motorTask->getAlpha());
        break;
    }
}
