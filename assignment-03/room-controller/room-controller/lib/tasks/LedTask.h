#ifndef __LEDTASK__
#define __LEDTASK__

#include "Task.h"
#include "Led.h"
#include "MessageTask.h"

class LedTask: public Task {

public:
  LedTask(MessageTask* mt);
  void tick() override;

private:
  enum {ON, OFF} _state;
  Led _led;
  MessageTask* _mt;
};

#endif
