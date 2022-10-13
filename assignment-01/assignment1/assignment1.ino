#include <avr/sleep.h>
#include <EnableInterrupt.h>

#define T1_PIN 2
#define L1_PIN 7
#define NUMBER 4
#define RED_LED 11
#define POT A1

int delta = 2;
int redledIntensity = 0;
int buttons[NUMBER];
int greenLeds[NUMBER];
volatile bool gameStarted = false;
bool pattern[NUMBER];
bool userPattern[NUMBER];
bool patternGenerated = false;
bool alreadyOutOfTime = false;
int penalty = 0;
int score = 0;
int time2 = 5000;
int time3 = 15000;
volatile int f = 1;
long initialTime = 0;
long startWaitingTime = 0;
bool sleeping = false;

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
  enableInterrupt(digitalPinToInterrupt(T1_PIN), t1Pressed, RISING);
  enableInterrupt(digitalPinToInterrupt(buttons[1]), wakeUp, RISING);
  enableInterrupt(digitalPinToInterrupt(buttons[2]), wakeUp, CHANGE);
  enableInterrupt(digitalPinToInterrupt(buttons[3]), wakeUp, CHANGE);

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
  
      if(!checkDefeat() && millis() - initialTime >= time3 && !alreadyOutOfTime){
        assignPenalty();
        alreadyOutOfTime = true;
        Serial.println("Out of time");    //debug
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
        time2 = time2 / (f + 1) + 150;
        time3 = time3 / (f + 1) + 1000;
        alreadyOutOfTime = false;
      }
      
      if(checkDefeat()){
        Serial.print("Game Over. Final Score: ");
        Serial.println(score);
        delay(10000);
        gameReset();
      }
    }
  }
}

void wakeUp(){}

void gameReset(){
  delta = 2;
  redledIntensity = 0;
  gameStarted = false;
  patternGenerated = false;
  alreadyOutOfTime = false;
  penalty = 0;
  score = 0;
  time2 = 5000;
  time3 = 15000;
  startWaitingTime = millis();
  Serial.println("Welcome to the Catch the Led Pattern Game. Press Key T1 to Start");
}

void waitForStart() {
  analogWrite(RED_LED, redledIntensity); 
  redledIntensity += delta;
  delta = redledIntensity >= 255 | redledIntensity <= 0 ? -delta : delta;
  delay(5);
  if(millis() - startWaitingTime >= /*10000*/3000){
    sleeping = true;
    goSleeping();
  }
}

void goSleeping(){
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  sleep_enable();
  sleep_mode();
  sleep_disable();
  sleeping = false;
  startWaitingTime = millis();
}

void t1Pressed(){
  if (!sleeping && !gameStarted){
    startGame();
  }
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
  for (int i=0; i<NUMBER;i++) {
    if (pattern[i])
      digitalWrite(greenLeds[i], HIGH);
    else digitalWrite(greenLeds[i], LOW);
  }
  delay(random(time2 / 2, time2));
  turnGreenLedsOff();
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
  delay(1000);
  digitalWrite(pin, LOW);
}

bool checkDefeat(){
  return (penalty >= 3);
}
