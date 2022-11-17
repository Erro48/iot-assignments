#ifndef __SONAR__
#define __SONAR__

class Sonar { 

public:
  Sonar(int echoPin, int trigPin);
  int getDistance();

private:
  int _echoPin;
  int _trigPin;
};

#endif
