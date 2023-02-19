#include <Arduino.h>
#include <constants.h>
#include "BluetoothTask.h"

BluetoothTask::BluetoothTask() :
  _msgServiceBT(P_BT_RX, P_BT_TX)
{
  _msgServiceBT.init();
}

void BluetoothTask::tick(){
  if (_msgServiceBT.isMsgAvailable()) {
    Msg* msg = _msgServiceBT.receiveMsg();
    Serial.println(msg->getContent());
    delete msg;
  }
}
