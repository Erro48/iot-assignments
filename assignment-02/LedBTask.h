#include "Task.h"
#include "Led.h"
#include "StateTask.h"

class LedBTask: public Task {

private:
  int _pin;
  Light* _led;
  enum {ON, OFF} _state;
  StateTask* _stateTask;
public:
  LedBTask(int pin, StateTask* stateTask);  
  void init(int period);
  void tick();
};
