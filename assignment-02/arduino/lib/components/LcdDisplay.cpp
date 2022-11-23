#include <Arduino.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "LcdDisplay.h"

LcdDisplay::LcdDisplay(int address, int rows, int cols) :
_lcd(0x27, cols, rows) {
  _lcd.init();
  _lcd.backlight();
  _lcd.clear();
  _lcd.setCursor(0, 0);
  _lcd.print("Dio Canaglia!");
}

void LcdDisplay::displayWaterLevel(int waterLevel) {
  // _lcd.clear();
  _lcd.setCursor(1,1);
  _lcd.print("Water level: " + String(waterLevel));
}

void LcdDisplay::displayMotorAlpha(int alpha) {
  // _lcd.clear();
  _lcd.setCursor(1,2);
  _lcd.print("Motor Alpha: " + String(alpha));
}

void LcdDisplay::clearDisplay() {
  _lcd.clear();
}