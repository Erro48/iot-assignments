#ifndef __LEDCTASK__
#define __LEDCTASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedCTask: public Task {

public:
  LedCTask(int pin, StateTask* stateTask);  
  void tick() override;

private:
  int _pin;
  enum {ON, OFF} _state;
  Led _led;
  StateTask* _stateTask;
};

#endif
