#include "game.h"

int delta = 2;
int redledIntensity = MIN_VALUE;
int buttons[NUMBER];
int greenLeds[NUMBER];
volatile bool gameStarted = false;
bool pattern[NUMBER];
bool userPattern[NUMBER];
bool patternGenerated = false;
bool alreadyOutOfTime = false;
int penalty = 0;
int score = 0;
int time2 = TIME2;
int time3 = TIME3;
volatile int f = 1;
long initialTime = 0;
long startWaitingTime = 0;
volatile bool sleeping = false;
volatile bool isRendering = false;

volatile long lastButtonPressedTime = 0;
void gameReset(){
  delta = 2;
  redledIntensity = 0;
  gameStarted = false;
  patternGenerated = false;
  alreadyOutOfTime = false;
  penalty = 0;
  score = 0;
  time2 = TIME2;
  time3 = TIME3;
  startWaitingTime = millis();
  Serial.println("Welcome to the Catch the Led Pattern Game. Press Key T1 to Start");
}

void waitForStart() {
  analogWrite(RED_LED, redledIntensity); 
  redledIntensity += delta;
  delta = redledIntensity >= MAX_VALUE | redledIntensity <= MIN_VALUE ? -delta : delta;
  delay(5);
  if(millis() - startWaitingTime >= DEEP_SLEEP_TIME){
    sleeping = true;
    goSleeping();
  }
}

void goSleeping(){
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  sleep_enable();
  sleep_mode();
  sleep_disable();
  startWaitingTime = millis();
}

void t1Pressed(){
  if (millis() - lastButtonPressedTime > DEBOUNCE_TIME) {
    if (!sleeping && !gameStarted){
      startGame();
    } else if(isRendering) {
      assignPenalty();
    } else {
      sleeping = false;
    }
  }

  lastButtonPressedTime = millis();
  
}

void buttonPressed(){
  if (millis() - lastButtonPressedTime > DEBOUNCE_TIME) {
    if (isRendering) {
      assignPenalty();
    } else {
      sleeping = false;
    }
    
  }
  lastButtonPressedTime = millis();
  
}

void startGame() {
  f = (analogRead(POT) / 256) + 1;    //Per la difficoltà
  gameStarted = true;
  digitalWrite(RED_LED, LOW);
  Serial.println("Go!");
  //Debug
  Serial.print("Difficoltà: ");
  Serial.println(f);
  //----
}

void generatePattern() {
  for (int i=0; i<NUMBER; i++) {
    if (random(2)) {
      pattern[i] = true;
    } else pattern[i] = false;
  }
  patternGenerated = true;
}

void render() {
  isRendering = true;
  for (int i=0; i<NUMBER;i++) {
    if (pattern[i])
      digitalWrite(greenLeds[i], HIGH);
    else digitalWrite(greenLeds[i], LOW);
  }
  delay(random(time2 / 2, time2));
  turnGreenLedsOff();
  isRendering = false;
}

void turnGreenLedsOff(){
  for (int i=0; i < NUMBER; i++) {
    digitalWrite(greenLeds[i], LOW);
  }
}

void listenButtons() {
  for (int i = 0; i < NUMBER; i++) {
    if(digitalRead(buttons[i]) == HIGH){
      if(!pattern[i]){
        assignPenalty();
      } else {
        digitalWrite(greenLeds[i], HIGH);
        userPattern[i] = true;
      }
    }
  }
}

bool checkWin(){
  for (int i = 0; i < NUMBER; i++) {
    if(pattern[i] != userPattern[i]){
      return false;
    }
  }
  return true;
}

void assignPenalty(){
  Serial.println("Penalty!");
  penalty++;
  blinkLed(RED_LED);
}

void blinkLed(int pin){
  digitalWrite(pin, HIGH);
  for(int i = 0; i < 100; i++){
    delayMicroseconds(10000);
  }
  digitalWrite(pin, LOW);
}

bool checkDefeat(){
  return (penalty >= MAX_PENALTIES);
}

long decreaseTime(long currentTime, int difficulty) {
  static int trialNumber = 1;
  long newTime = currentTime - floor((difficulty * REDUCTION_TIME_FACTOR) / trialNumber++);
  return newTime <= 500 ? 500 : newTime;
}
