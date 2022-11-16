#ifndef __SCHEDULER__
#define __SCHEDULER__

#include <constants.h>
#include "Task.h"



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
