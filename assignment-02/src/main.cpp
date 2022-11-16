#include <Arduino.h>

#define LED 13
#define DELAY 100

void setup() {
  pinMode(LED, OUTPUT);
}

void loop() {
  digitalWrite(LED, HIGH);
  delay(DELAY);
  digitalWrite(LED, LOW);
  delay(DELAY);
}