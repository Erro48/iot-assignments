#ifndef __LEDBTASK__
#define __LEDBTASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedBTask: public Task {

public:
  LedBTask(StateTask* stateTask);  
  void tick() override;

private:
  enum {ON, OFF} _state;
  Led _led;
  StateTask* _stateTask;

};

#endif