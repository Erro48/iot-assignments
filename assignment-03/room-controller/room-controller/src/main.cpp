#include <Arduino.h>
#include <Scheduler.h>
#include <constants.h>
#include <tasks.h>

Scheduler s;

void setup() {
  Serial.begin(9600);

  s.init(SCHEDULER_PERIOD);

  MessageTask* messageTask = new MessageTask();
  messageTask->init(TASK_PERIOD);

  BluetoothTask* bluetoothTask = new BluetoothTask();
  bluetoothTask->init(TASK_PERIOD);

  Task* ledTask = new LedTask(messageTask);
  ledTask->init(TASK_PERIOD);

  MotorTask* motorTask = new MotorTask(messageTask);
  motorTask->init(TASK_PERIOD);

  s.addTask(messageTask);
  s.addTask(bluetoothTask);
  s.addTask(ledTask);
  s.addTask(motorTask);
}

void loop() {
  s.schedule();
}