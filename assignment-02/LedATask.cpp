#include "LedATask.h"

LedATask::LedATask(int pin, StateTask* stateTask){
  this->pin = pin;
  this->stateTask = stateTask;
}
  
void LedATask::init(int period){
  Task::init(period);
  led = new Led(pin); 
  state = OFF;
}
  
void LedATask::tick(){
  if(stateTask->getState() != 2){
    
  }
}
