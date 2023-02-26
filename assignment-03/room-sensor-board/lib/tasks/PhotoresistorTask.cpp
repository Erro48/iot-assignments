#include <Arduino.h>
#include "PhotoresistorTask.h"

#define PIN_PHOTORESISTOR 6

PhotoresistorTask::PhotoresistorTask(int *luminosity) :
    _photoresistor(PIN_PHOTORESISTOR)
{
    _luminosity = luminosity;
}

void PhotoresistorTask::tick() {
    *_luminosity = _photoresistor.getLuminosity();
}