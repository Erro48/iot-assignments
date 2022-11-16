#include "Task.h"
#include "Led.h"
#include "StateTask.h"
#include "Photoresistor.h"
#include "Pir.h"

class LedATask: public Task {

private:
  int _pin;
  Light* _led;
  enum {ON, OFF} _state;
  StateTask* _stateTask;
  Pir* _pir;
  Photoresistor* _photoresistor;
public:
  LedATask(int pin, StateTask* stateTask);  
  void init(int period);
  void tick();
};
