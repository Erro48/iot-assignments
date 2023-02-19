#include <Arduino.h>
#include <constants.h>
#include "LedTask.h"
#include <MsgService.h>

LedTask::LedTask(MessageTask* mt) :
  _led(P_LED)
{
  _state = OFF;
  _mt = mt;
}

  
void LedTask::tick(){
  if(_mt->isMessageAvailable()){
      String msg = _mt->getMessage();
      if(msg.charAt(0) == 'L'){
        if(msg.substring(1).compareTo("true") == 0){
          _led.switchOn();
          _state = ON;
        } else if(msg.substring(1).compareTo("false") == 0){
          _led.switchOff();
          _state = OFF;
        }
        _mt->messageActionDone();
      }
    }
}
