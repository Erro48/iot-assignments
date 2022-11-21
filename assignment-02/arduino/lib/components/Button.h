#ifndef __BUTTON__
#define __BUTTON__

class Button {

public:
    Button(int pin);
    bool isPressed();
private:
    int _pin;
    bool _state;
    int _lastTimePressed;
};

#endif

