#ifndef __TASK__
#define __TASK__

class Task {
  
public:
  virtual void init(int period){
    _myPeriod = period;  
    _timeElapsed = 0;
    _active = true;
  }

  virtual void tick() = 0;

  bool updateAndCheckTime(int basePeriod){
    _timeElapsed += basePeriod;
    if (_timeElapsed >= _myPeriod){
      _timeElapsed = 0;
      return true;
    } else {
      return false; 
    }
  }

  bool isActive(){
    return _active;
  }

  void setActive(bool active){
    this->_active = active;
  }

private:
  int _myPeriod;
  int _timeElapsed;
  bool _active;

};

#endif
