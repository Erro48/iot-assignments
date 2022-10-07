#include <avr/sleep.h>

#define T1_PIN 2
#define L1_PIN 7
#define NUMBER 4
#define RED_LED 11
#define POT A1

int delta = 2;
int redledIntensity = 0;
int buttons[NUMBER];
int greenLeds[NUMBER];
int startTime = 0;
bool gameStarted = false;
bool pattern[NUMBER];
bool userPattern[NUMBER];
bool patternGenerated = false;
bool outOfTime = false;
int penalty = 0;
int score = 0;
int time2 = 5000;
int time3 = 15000;
int f = 1;
long initialTime = 0;

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

  pinMode(POT, INPUT);
  
  Serial.println("Welcome to the Catch the Led Pattern Game. Press Key T1 to Start");
}

void loop() {
  if (!gameStarted) {
    waitForStart();
  }else {
    if (!patternGenerated){
      generatePattern(); 
      render();
      initialTime = millis();
    }
    
    listenButtons();

    if(millis() - initialTime >= time3 && !outOfTime){
      assignPenalty();
      outOfTime = true;
      Serial.println("Out of time");    //debug
    }
    
    if(checkWin()){
      score++;
      Serial.print("New point! Score: ");
      Serial.println(score);
      patternGenerated = false;
      for(int i = 0; i < NUMBER; i++){
        userPattern[i] = false;
      }
      time2 = time2 / f;
      time3 = time3 / f;
      outOfTime = false;
    }
    
    if(checkDefeat()){
      Serial.print("Game Over. Final Score: ");
      Serial.println(score);
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
  f = (analogRead(POT) / 256) + 1;    //Per la difficoltà
  gameStarted = true;
  digitalWrite(RED_LED, LOW);
  turnGreenLedsOff();   //Per sicurezza (?)
  Serial.println("Go!");
  //Debug
  Serial.print("Difficoltà: ");
  Serial.println(f);
  //----
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
