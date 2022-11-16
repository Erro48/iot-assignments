#include "Scheduler.h"
#include <TimerOne.h>

volatile bool _timerFlag;

void timerHandler(void){
  _timerFlag = true;
}

void Scheduler::init(int basePeriod){
  _basePeriod = basePeriod;
  _timerFlag = false;
  long period = 1000l*basePeriod;
  Timer1.initialize(period);
  Timer1.attachInterrupt(timerHandler);
  _nTasks = 0;
}

bool Scheduler::addTask(Task* task){
  if (_nTasks < MAX_TASKS-1){
    _taskList[_nTasks] = task;
    _nTasks++;
    return true;
  } else {
    return false; 
  }
}
  
void Scheduler::schedule(){   
  while (!_timerFlag){}
  _timerFlag = false;

  for (int i = 0; i < _nTasks; i++){
    if (_taskList[i]->isActive() && _taskList[i]->updateAndCheckTime(_basePeriod)){
      _taskList[i]->tick();
    }
  }
}
