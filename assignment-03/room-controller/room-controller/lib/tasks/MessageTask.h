#ifndef __MESSAGETASK__
#define __MESSAGETASK__

#include "Task.h"
#include <MsgService.h>

class MessageTask: public Task {

public:
  MessageTask();
  void tick() override;
  bool isMessageAvailable();
  String getMessage();
  void messageActionDone();

private:
  String _message;
};

#endif
