#ifndef __LEDATASK__
#define __LEDATASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"
#include "Photoresistor.h"
#include "Pir.h"

class LedATask: public Task {

public:
  LedATask(int pin, StateTask* stateTask);  
  void init(int period);
  void tick();

private:
  int _pin;
  enum {ON, OFF} _state;
  Light* _led;
  StateTask* _stateTask;
  Pir* _pir;
  Photoresistor* _photoresistor;
};

#endif
