#ifndef __PHOTORESISTORTASK__
#define __PHOTORESISTORTASK__

#include "Task.h"
#include "Photoresistor.h"

class PhotoresistorTask: public Task {
public:
    PhotoresistorTask(int *luminosity);
    void tick() override;

private:
    Photoresistor _photoresistor;
    int *_luminosity;
};

#endif