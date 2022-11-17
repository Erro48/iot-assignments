#ifndef __MOTORTASK__
#define __MOTORTASK__

#include <Servo.h>
#include "Task.h"
#include "Sonar.h"
#include "StateTask.h"

class MotorTask: public Task {

public:
    MotorTask(int pin, StateTask* stateTask);
    void tick() override;

private:
    int _pin;
    StateTask* _stateTask;
    int _lastDistance;
    int _alpha;
    Servo _servo;
    Sonar* _sonar;
    int _temp;
};

#endif