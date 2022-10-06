#include <avr/sleep.h>

#define T1_PIN 2
#define L1_PIN 7
#define NUMBER 4
#define RED_LED 11

int delta = 2;
int redledIntensity = 0;
int buttons[NUMBER];
int greenLeds[NUMBER];
int startTime = 0;
bool gameStarted = false;
bool pattern[NUMBER];
bool userPattern[NUMBER];
bool patternGenerated = false;
int penalty = 0;

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
  attachInterrupt(digitalPinToInterrupt(buttons[0]), startGame, RISING);
  
  Serial.println("Welcome to the Catch the Led Pattern Game. Press Key T1 to Start");
}

void loop() {
  if (!gameStarted) {
    waitForStart();
  }else {
    if (!patternGenerated){
      generatePattern(); 
      render();
    }
    
    listenButtons();
    if(checkWin()){
      Serial.println("New point! Score: boh");
      patternGenerated = false;
      for(int i = 0; i < NUMBER; i++){
        userPattern[i] = false;
      }
    }
  }
}

void waitForStart() {
    analogWrite(RED_LED, redledIntensity); 
    redledIntensity += delta;
    delta = redledIntensity >= 255 | redledIntensity <= 0 ? -delta : delta;
    delay(5);
}

void startGame() {
  gameStarted = true;
  digitalWrite(RED_LED, LOW);
  turnGreenLedsOff();   //Per sicurezza (?)
  Serial.println("Go!");
  detachInterrupt(digitalPinToInterrupt(T1_PIN));
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
  delay(random(1000, 5000));
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
