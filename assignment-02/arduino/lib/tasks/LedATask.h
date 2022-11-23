#ifndef __LEDATASK__
#define __LEDATASK__

#include "Task.h"
#include "Led.h"
#include "StateTask.h"
#include "Photoresistor.h"
#include "Pir.h"

class LedATask: public Task {

public:
  LedATask(StateTask* stateTask);  
  void tick() override;

private:
  enum {ON, OFF} _state;
  Led _led;
  StateTask* _stateTask;
  Pir _pir;
  Photoresistor _photoresistor;
};

#endif
