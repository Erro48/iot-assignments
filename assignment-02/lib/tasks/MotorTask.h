#ifndef __MOTORTASK__
#define __MOTORTASK__

#include <Servo.h>
#include "Task.h"
#include "Sonar.h"

class MotorTask: public Task {

public:
    MotorTask(int pin);
    void tick();

private:
    int _pin;
    int _lastDistance;
    int _alpha;
    Servo _servo;
    Sonar* _sonar;
};

#endif