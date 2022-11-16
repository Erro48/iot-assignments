#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Task.h"

#define MAX_TASKS 50

class Scheduler {  

public:
  void init(int basePeriod);  
  virtual bool addTask(Task* task);  
  virtual void schedule();

private:
  int _basePeriod;
  int _nTasks;
  Task* _taskList[MAX_TASKS];
};

#endif
