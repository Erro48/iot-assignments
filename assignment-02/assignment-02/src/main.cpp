#include <Arduino.h>

#define LED_BUILTIN 13
#define DELAY 1000

void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  digitalWrite(LED_BUILTIN, HIGH);
  delay(DELAY);
  
  digitalWrite(LED_BUILTIN, LOW);
  delay(DELAY);
}