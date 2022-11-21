#ifndef __MOTORMODETASK__
#define __MOTORMODETASK__

#include "Task.h"
#include "StateTask.h"
#include "Button.h"

class MotorModeTask: public Task {

public:
    enum MotorMode {
        AUTO,
        MANUAL,
        CONSOLE
    };
    MotorModeTask(StateTask* stateTask);
    void tick() override;
    MotorMode getMotorMode();

private:
    StateTask* _stateTask;
    Button* _button;
    MotorMode _motorMode;
    bool _currentBtnState;
    bool _lastBtnState;
};

#endif
