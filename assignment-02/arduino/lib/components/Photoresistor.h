#ifndef __PHOTORESISTOR__
#define __PHOTORESISTOR__

class Photoresistor { 

public:
  Photoresistor(int pin);
  int getLuminosity();

private:
  int _pin;
};

#endif
