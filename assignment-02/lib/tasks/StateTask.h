#ifndef __STATE_TASK__
#define __STATE_TASK__

#include "Task.h"
#include "Sonar.h"

class StateTask: public Task {

public:
  void init(int period);
  void tick();
  int getState();

private:
  int _state;
  Sonar* _sonar;
};

#endif
