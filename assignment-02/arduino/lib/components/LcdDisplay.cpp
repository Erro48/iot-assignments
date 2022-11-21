#include <Arduino.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "LcdDisplay.h"

LcdDisplay::LcdDisplay(int address, int rows, int cols) {
  _lcd = new LiquidCrystal_I2C(0x27, cols, rows);
  _lcd->begin(cols, rows);
  _lcd->backlight();
  _lcd->clear();
  _lcd->setCursor(4,0);

  Serial.println("asdf");

  _lcd->print("Dio Bestia");
}

