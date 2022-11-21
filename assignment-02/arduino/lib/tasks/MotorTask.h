#ifndef __MOTORTASK__
#define __MOTORTASK__

#include <Servo.h>
#include "Task.h"
#include "Sonar.h"
#include "Potentiometer.h"
#include "StateTask.h"
#include "MotorModeTask.h"

class MotorTask: public Task {

public:
    MotorTask(int pin, StateTask* stateTask, MotorModeTask* motorModeTask);
    void tick() override;

private:
    int _pin;
    StateTask* _stateTask;
    int _lastDistance;
    int _alpha;
    Servo _servo;
    Sonar* _sonar;
    Potentiometer* _pot;
    MotorModeTask* _motorModeTask;
    MotorModeTask::MotorMode _motorMode;

    void autoMode();
    void manualMode();
    void consoleMode();
};

#endif