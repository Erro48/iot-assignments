#include <Arduino.h>
#include <Scheduler.h>
#include <constants.h>
#include "Task.h"
#include "LedATask.h"
#include "LedBTask.h"
#include "LedCTask.h"
#include "MotorTask.h"
#include "StateTask.h"


Scheduler s;

void setup() {
  Serial.begin(9600);
  
  s.init(SCHEDULER_PERIOD);

  StateTask* st = new StateTask();
  st->init(TASK_PERIOD);
  
  Task* la = new LedATask(P_LED_A, st);
  la->init(TASK_PERIOD);

  Task* lb = new LedBTask(P_LED_B, st);
  lb->init(TASK_PERIOD);

  Task* lc = new LedCTask(P_LED_C, st);
  lc->init(T_LED_C_PERIOD);

  Task* m = new MotorTask(P_MOTOR, st);
  m->init(TASK_PERIOD);
  
  s.addTask(st);
  s.addTask(la);
  s.addTask(lb);
  s.addTask(lc);
  s.addTask(m);
}

void loop() {
  s.schedule();
}
