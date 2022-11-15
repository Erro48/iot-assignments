#ifndef __PIR__
#define __PIR__

class Pir { 
private:
  int _pin;
public:
  Pir(int pin);
  int isTriggered();
};

#endif
