#ifndef __MOTORTASK__
#define __MOTORTASK__

#include <Servo.h>
#include "Task.h"
#include "MessageTask.h"

class MotorTask: public Task {

public:
    MotorTask(MessageTask* mt);
    void tick() override;
    //int getAlpha();

private:
    int _alpha;
    Servo _servo;
    MessageTask* _mt;
};

#endif