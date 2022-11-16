#ifndef __MOTOR__
#define __MOTOR__

class Motor { 

public:
  Led(int pin);
  void switchOn();
  void switchOff();    

private:
  int _pin;
};

#endif
