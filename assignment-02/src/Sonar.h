#ifndef __SONAR__
#define __SONAR__

class Sonar { 
private:
  int _echoPin;
  int _trigPin;
public:
  Sonar(int echoPin, int trigPin);
  int getDistance();
};

#endif
