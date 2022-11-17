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
  
  s.init(100);

  StateTask* st = new StateTask();
  st->init(100);
  
  Task* la = new LedATask(P_LED_A, st);
  la->init(100);

  Task* lb = new LedBTask(P_LED_B, st);
  lb->init(100);

  Task* lc = new LedCTask(P_LED_C, st);
  lc->init(100);

  Task* m = new MotorTask(P_MOTOR, st);
  m->init(100);
  
  s.addTask(st);
  s.addTask(la);
  s.addTask(lb);
  s.addTask(lc);
  s.addTask(m);
}

void loop() {
  Serial.println("Main Loop...");
  s.schedule();
}
