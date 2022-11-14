#ifndef __STATE_TASK__
#define __STATE_TASK__

#include "Task.h"

class StateTask: public Task {

private:
  int state;
    
public:
  void init(int period);
  void tick();
  int getState();
};

#endif
