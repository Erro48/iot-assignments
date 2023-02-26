#include "Scheduler.h"
#include "esp32-hal-timer.h"


hw_timer_t *timer = NULL;
volatile bool _timerFlag;

void IRAM_ATTR timerHandler(void){
  _timerFlag = true;
}

void Scheduler::init(int basePeriod){
  _basePeriod = basePeriod;
  _timerFlag = false;
  long period = 1000l * basePeriod;

  timer = timerBegin(0, 80, true);
  timerAttachInterrupt(timer, &timerHandler, true);
  timerAlarmWrite(timer, period, true); 
  timerAlarmEnable(timer);
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
