#ifndef __LEDCTASK__
#define __LEDCTASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedCTask: public Task {

public:
  LedCTask(StateTask* stateTask);  
  void tick() override;

private:
  enum {ON, OFF} _state;
  Led _led;
  StateTask* _stateTask;
};

#endif
