#include <Arduino.h>
#include "LedATask.h"

#define PIR 9
#define PHOTORESISTOR A0
#define MIN_LUM 650

LedATask::LedATask(int pin, StateTask* stateTask){
  this->_pin = pin;
  this->_stateTask = stateTask;
}
  
void LedATask::init(int period){
  Task::init(period);
  this->_led = new Led(_pin);
  this->_photoresistor = new Photoresistor(PHOTORESISTOR);
  this->_pir = new Pir(PIR);
  this->_state = OFF;
}
  
void LedATask::tick(){
  Serial.println(this->_pir->isTriggered());
  Serial.println(this->_photoresistor->getLuminosity());
  if(this->_stateTask->getState() != 2){
    if(this->_state != ON/* && this->_pir->isTriggered()*/ && this->_photoresistor->getLuminosity() < MIN_LUM){
      this->_led->switchOn();
      this->_state = ON;
    }
    if(this->_state != OFF && (/*!this->_pir->isTriggered() || */this->_photoresistor->getLuminosity() >= MIN_LUM)){
      this->_led->switchOff();
      this->_state = OFF;
    }
  } else {
    if(this->_state == ON){
      this->_led->switchOff();
    }
  }
}
