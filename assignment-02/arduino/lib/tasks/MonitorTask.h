#ifndef __MONITOR_TASK__
#define __MONITOR_TASK__

#include "Task.h"
#include "Sonar.h"

class MonitorTask: public Task {

public:
  MonitorTask();
  void tick() override;

private:
  Sonar _sonar;
};

#endif
