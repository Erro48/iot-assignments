#include <Arduino.h>
#include <constants.h>
#include "MessageTask.h"

MessageTask::MessageTask() :
  _message()
{
}

void MessageTask::tick(){
  if (MsgService.isMsgAvailable()) {
      Msg* msg = MsgService.receiveMsg();
      _message = msg->getContent();
      Serial.println(_message);
      delete msg;
  }
}

bool MessageTask::isMessageAvailable(){
  if(_message.compareTo("") != 0){
    return true;
  }
  return false;
}

String MessageTask::getMessage(){
  return _message;
}

void MessageTask::messageActionDone(){
  _message = "";
}