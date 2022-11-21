#ifndef __LCDDISPLAY_TASK__
#define __LCDDISPLAY_TASK__

#include "Task.h"
#include "LcdDisplay.h"
#include "StateTask.h"
#include "MotorTask.h"
#include "Sonar.h"

class LcdDisplayTask : public Task { 

public:
    LcdDisplayTask(StateTask* stateTask, MotorTask* motorTask);
    void tick() override;

private:
    LcdDisplay _lcd;
    Sonar _sonar;
    StateTask* _stateTask;
    MotorTask* _motorTask;
};

#endif
