#include <Arduino.h>
#include <constants.h>
#include "MotorTask.h"
#include <MsgService.h>


MotorTask::MotorTask(MessageTask* mt)
{
    pinMode(P_MOTOR, OUTPUT);
    _alpha = MOTOR_MIN_ALPHA;
    _servo.attach(P_MOTOR);
    _mt = mt;
}

void MotorTask::tick() {
    if(_mt->isMessageAvailable()){
        String msg = _mt->getMessage();
        if(msg.charAt(0) == 'M'){
            _mt->messageActionDone();
            _alpha = msg.substring(1).toInt();
            _servo.write(_alpha);
        }
    }
}

/*int MotorTask::getAlpha() {
    return _alpha;
}*/