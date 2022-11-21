#ifndef __POTENTIOMETER__
#define __POTENTIOMETER__

class Potentiometer {

public:
    Potentiometer(int pin);
    int read();

private:
    int _pin;
};

#endif