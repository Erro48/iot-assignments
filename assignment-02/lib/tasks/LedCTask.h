#ifndef __LEDCTASK__
#define __LEDCTASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedCTask: public Task {

public:
  LedCTask(int pin, StateTask* stateTask);  
  void init(int period);
  void tick();

private:
  int _pin;
  enum {ON, OFF} _state;
  Light* _led;
  StateTask* _stateTask;
};

#endif
