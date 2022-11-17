#include <Arduino.h>
#include <Scheduler.h>
#include "Task.h"
#include "LedATask.h"
#include "LedBTask.h"
#include "LedCTask.h"
#include "StateTask.h"


#define LED_A 4
#define LED_B 3
#define LED_C 5

Scheduler s;

void setup() {
  Serial.begin(9600);
  
  s.init(100);

  StateTask* st = new StateTask();
  st->init(100);
  
  Task* la = new LedATask(LED_A, st);
  la->init(100);

  Task* lb = new LedBTask(LED_B, st);
  lb->init(100);

  Task* lc = new LedCTask(LED_C, st);
  lc->init(100);
  
  s.addTask(st);
  s.addTask(la);
  s.addTask(lb);
  s.addTask(lc);
}

void loop() {
  s.schedule();
}
