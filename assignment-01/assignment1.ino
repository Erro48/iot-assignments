/*
Group's members:
- Lorenzo Guerrini: lorenzo.guerrini5@studio.unibo.it
- Marco Sternini: marco.sternini@studio.unibo.it
- Enrico Tagliaferri: enrico.tagliaferri2@studio.unibo.it
*/
#include <EnableInterrupt.h>
#include "game.h"

extern int buttons[COMPONENTS_NUMBER];
extern int greenLeds[COMPONENTS_NUMBER];
extern bool pattern[COMPONENTS_NUMBER];
extern long startWaitingTime;
extern bool gameStarted;
extern bool patternGenerated;
extern long initialTime;
extern int time2;
extern int time3;
extern int score;
extern bool userPattern[COMPONENTS_NUMBER];
extern int f;

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));

  pinMode(RED_LED, OUTPUT);
  for (int i=0; i < COMPONENTS_NUMBER; i++) {
    buttons[i] = T1_PIN + i; 
    pinMode(T1_PIN + i, INPUT);
    greenLeds[i] = L1_PIN + i;
    pinMode(L1_PIN + i, OUTPUT);
    pattern[i] = false;
  }
  enableInterrupt(buttons[0], t1Pressed, RISING);
  enableInterrupt(buttons[1], buttonPressed, RISING);
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
        patternGenerated = false;
      }
      
      if(checkWin()){
        score++;
        Serial.print("New point! Score: ");
        Serial.println(score);
        delay(500);
        patternGenerated = false;
        for(int i = 0; i < COMPONENTS_NUMBER; i++){
          userPattern[i] = false;
        }
        
        time2 = decreaseTime(time2, f);
        time3 = decreaseTime(time3, f);
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
