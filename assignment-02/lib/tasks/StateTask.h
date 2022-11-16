#ifndef __STATE_TASK__
#define __STATE_TASK__

#include "Task.h"
#include "Sonar.h"

class StateTask: public Task {

public:
  enum DeviceState {
    NORMAL,
    PREALARM,
    ALARM
  };

  void init(int period);
  void tick();
  DeviceState getState();

private:
  DeviceState _state;
  Sonar* _sonar;
};

#endif
