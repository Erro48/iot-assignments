#ifndef __LCDDISPLAY__
#define __LCDDISPLAY__

#include "LcdDisplay.h"
#include <LiquidCrystal_I2C.h>

class LcdDisplay { 

public:
  LcdDisplay(int address, int rows, int cols);

private:
    LiquidCrystal_I2C* _lcd;
};

#endif