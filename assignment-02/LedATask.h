#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedATask: public Task {

  int pin;
  Light* led;
  enum {ON, OFF} state;
  StateTask* stateTask;
  
public:

  LedATask(int pin, StateTask* stateTask);  
  void init(int period);  
  void tick();
};
