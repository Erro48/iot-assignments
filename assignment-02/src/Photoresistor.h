#ifndef __PHOTORESISTOR__
#define __PHOTORESISTOR__

class Photoresistor { 
private:
  int _pin;
public:
  Photoresistor(int pin);
  int getLuminosity();
};

#endif
