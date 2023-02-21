#ifndef __PIR__
#define __PIR__

class Pir { 

public:
  Pir(int pin);
  int isTriggered();
  // unsigned long getLastTrigger();
  // void updateLastTrigger();

private:
  int _pin;
  unsigned long _lastTrigger;
};

#endif
