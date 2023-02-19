#ifndef __BLUETOOTHTASK__
#define __BLUETOOTHTASK__

#include "Task.h"
//#include "SoftwareSerial.h"
#include "MsgServiceBT.h"

class BluetoothTask: public Task {

public:
  BluetoothTask();
  void tick() override;

private:
  MsgServiceBT _msgServiceBT;
};

#endif
