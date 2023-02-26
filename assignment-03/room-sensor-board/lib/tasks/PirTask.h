#ifndef __PIRTASK__
#define __PIRTASK__

#include "Task.h"
#include "Led.h"
#include "Pir.h"

class PirTask: public Task {

public:
  PirTask(bool *someoneIn);  
  void tick() override;

private:
  // enum {ON, OFF} _ledState;
  bool *_someoneIn;
  bool _lastTriggerStatus;
  int _lastTrigger;
  Led _led;
  Pir _pir;
};

#endif
