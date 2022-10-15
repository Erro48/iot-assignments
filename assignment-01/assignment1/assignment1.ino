#include <avr/sleep.h>
#include <EnableInterrupt.h>

#define T1_PIN 2
#define L1_PIN 7
#define NUMBER 4
#define RED_LED 11
#define POT A1

#define MAX_PENALTIES 3
#define MAX_VALUE 255
#define MIN_VALUE 0

#define DEEP_SLEEP_TIME 10 * 1000 // in milliseconds
#define AFTER_GAME_END_TIME 10 * 1000
#define DEBOUNCE_TIME 200
#define TIME2 5000
#define TIME3 7000
#define REDUCTION_TIME_FACTOR 500

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

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));

  pinMode(RED_LED, OUTPUT);
  for (int i=0; i < NUMBER; i++) {
    buttons[i] = T1_PIN + i; 
    pinMode(T1_PIN + i, INPUT);
    greenLeds[i] = L1_PIN + i;
    pinMode(L1_PIN + i, OUTPUT);
    pattern[i] = false;
  }
  enableInterrupt(T1_PIN, t1Pressed, RISING);
  enableInterrupt(T1_PIN + 1, buttonPressed, RISING);
  enableInterrupt(buttons[2], buttonPressed, CHANGE);
  enableInterrupt(buttons[3], buttonPressed, CHANGE);

  pinMode(POT, INPUT);

  startWaitingTime = millis();
  
  Serial.println("Welcome to the Catch the Led Pattern Game. Press Key T1 to Start");
}

void loop() {
  if(!checkDefeat()){
    if (!gameStarted) {
      waitForStart();
    }else {
      if (!patternGenerated){
        generatePattern();
        turnGreenLedsOff();
        delay(random(500, 3000));  //time1
        render();
        initialTime = millis();
      }
      
      listenButtons();
  
      if(!checkDefeat() && millis() - initialTime >= time3){
        assignPenalty();
        Serial.println("Out of time");    //debug
        patternGenerated = false;
      }
      
      if(checkWin()){
        score++;
        Serial.print("New point! Score: ");
        Serial.println(score);
        delay(500);
        patternGenerated = false;
        for(int i = 0; i < NUMBER; i++){
          userPattern[i] = false;
        }
        //time2 = time2 / (f + 1) + 150;
        //time3 = time3 / (f + 1) + 1000;
        time2 = decreaseTime(time2, f);
        time3 = decreaseTime(time3, f);

        Serial.println(time2);
        Serial.println(time3);
        alreadyOutOfTime = false;
      }
      
      if(checkDefeat()){
        Serial.print("Game Over. Final Score: ");
        Serial.println(score);
        turnGreenLedsOff();
        delay(AFTER_GAME_END_TIME);
        gameReset();
      }
    }
  }
}

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
