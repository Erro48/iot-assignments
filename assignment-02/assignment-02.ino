#include "Scheduler.h"
#include "Task.h"
#include "LedATask.h"
#include "StateTask.h"

#define LEDA 4

Scheduler s;

void setup() {
  Serial.begin(9600);
  
  s.init(100);

  StateTask* st = new StateTask();
  st->init(100);
  
  Task* la = new LedATask(LEDA, st);
  la->init(100);

  s.addTask(st);
  s.addTask(la);
}

void loop() {
  s.schedule();  
}
