#include <Arduino.h>
#include <Scheduler.h>
#include <tasks.h>

Scheduler s;

#define SCHEDULER_PERIOD 10
#define TASK_PERIOD 100

void setup() {
  Serial.begin(9600);

  s.init(SCHEDULER_PERIOD);

  LedTask* ledTask = new LedTask();
  ledTask->init(TASK_PERIOD);

  // StateTask* stateTask = new StateTask();
  // stateTask->init(NORMAL_SAMPLING_PERIOD);
  
  // MotorModeTask* motorModeTask = new MotorModeTask(stateTask);
  // motorModeTask->init(TASK_PERIOD);
  
  // Task* ledATask = new LedATask(stateTask);
  // ledATask->init(TASK_PERIOD);

  // Task* ledBTask = new LedBTask(stateTask);
  // ledBTask->init(TASK_PERIOD);

  // Task* ledCTask = new LedCTask(stateTask);
  // ledCTask->init(LED_C_PERIOD);

  // MotorTask* motorTask = new MotorTask(stateTask, motorModeTask);
  // motorTask->init(TASK_PERIOD);

  // Task* monitorTask = new MonitorTask();
  // monitorTask->init(MONITOR_PERIOD);

  // Task* lcdTask = new LcdDisplayTask(stateTask, motorTask);
  // lcdTask->init(MONITOR_PERIOD);

  // s.addTask(stateTask);
  // s.addTask(ledATask);
  // s.addTask(ledBTask);
  // s.addTask(ledCTask);
  // s.addTask(motorTask);
  // s.addTask(motorModeTask);
  // s.addTask(monitorTask);
  // s.addTask(lcdTask);

  s.addTask(ledTask);
}

void loop() {
  Serial.println("Loop");
  s.schedule();
}
