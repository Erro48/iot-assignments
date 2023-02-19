#include <Arduino.h>
#include "Msg.h"

Msg::Msg(const String& content)
{
    _content = content;
}
  
String Msg::getContent(){
    return _content;
}