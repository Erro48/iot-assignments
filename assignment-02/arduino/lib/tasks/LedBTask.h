#ifndef __LEDBTASK__
#define __LEDBTASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedBTask: public Task {

public:
  LedBTask(int pin, StateTask* stateTask);  
  void tick() override;

private:
  int _pin;
  enum {ON, OFF} _state;
  Light* _led;
  StateTask* _stateTask;

};

#endif