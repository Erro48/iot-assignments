#include <Arduino.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "LcdDisplay.h"

LcdDisplay::LcdDisplay(int address, int rows, int cols) :
_lcd(0x27, cols, rows) {
  _lcd.init();
  _lcd.backlight();
  _state = HIGH;
  _lcd.clear();
  _lcd.setCursor(0, 0);
  _lcd.print("Setup Done");
}

void LcdDisplay::displayWaterLevel(int waterLevel) {
  _lcd.setCursor(1,1);
  _lcd.print("Water Level: " + String(waterLevel) + "m");
}

void LcdDisplay::displayMotorAlpha(int alpha) {
  _lcd.setCursor(1,2);
  _lcd.print("Motor Alpha: " + String(alpha) + (char)223);
}

void LcdDisplay::clearDisplay() {
  _lcd.clear();
}

void LcdDisplay::switchOn(bool value) {
  _state = value;
  if (_state) _lcd.backlight();
  else _lcd.noBacklight();
}

bool LcdDisplay::isSwitchedOff() {
  return !_state;
}