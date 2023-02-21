#ifndef __LEDATASK__
#define __LEDATASK__

#include "Task.h"
#include "Led.h"
#include "Pir.h"

class LedTask: public Task {

public:
  LedTask();  
  void tick() override;

private:
  // enum {ON, OFF} _ledState;
  bool _someoneIn;
  Led _led;
  Pir _pir;
};

#endif
