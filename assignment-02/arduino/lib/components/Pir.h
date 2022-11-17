#ifndef __PIR__
#define __PIR__

class Pir { 

public:
  Pir(int pin);
  int isTriggered();

private:
  int _pin;
};

#endif
