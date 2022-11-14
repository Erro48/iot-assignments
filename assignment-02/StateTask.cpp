#include <Arduino.h>
#include "StateTask.h"

#define SONAR_ECHO 8
#define SONAR_TRIG 7
#define WL1 800
#define WL2 1500

/*StateTask::StateTask(){
}*/
  
void StateTask::init(int period){
  Task::init(period);
  this->state = 0;
}
  
void StateTask::tick(){
  digitalWrite(SONAR_TRIG, LOW);
  delayMicroseconds(3);
  int distance = pulseIn(SONAR_ECHO, HIGH);
  if(distance < WL1){
    this->state = 0;
  } else if (distance >= WL1 && distance < WL2){
    this->state = 1;
  } else {
    this->state = 2;
  }
  Serial.println(this->state);
}

int StateTask::getState() {
  return this->state;
}
