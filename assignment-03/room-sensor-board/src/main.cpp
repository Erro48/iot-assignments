#include <Arduino.h>
#include <Scheduler.h>
#include <tasks.h>

Scheduler s;

#define SCHEDULER_PERIOD 10
#define TASK_PERIOD 100
#define PHOTORESISTOR_TASK_PERIOD 1000

bool *someoneIn;
int *luminosity;

void setup() {
  Serial.begin(9600);
  someoneIn = (bool*) malloc(sizeof(bool));
  luminosity = (int*) malloc(sizeof(int));

  *someoneIn = false;
  *luminosity = 0;

  s.init(SCHEDULER_PERIOD);

  PirTask* pirTask = new PirTask(someoneIn);
  pirTask->init(TASK_PERIOD);
  PhotoresistorTask* photoTask = new PhotoresistorTask(luminosity);
  photoTask->init(TASK_PERIOD);

  ConnectionTask* connectionTask = new ConnectionTask(someoneIn, luminosity);
  connectionTask->init(TASK_PERIOD);

  s.addTask(pirTask);
  s.addTask(photoTask);
  s.addTask(connectionTask);
  // pinMode(4, OUTPUT);
  // digitalWrite(4, HIGH);
}

void loop() {
  s.schedule();
  // delay(1000);
}
