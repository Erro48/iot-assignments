#include <Arduino.h>
#include <avr/sleep.h>

#define T1_PIN 2
#define L1_PIN 7
#define RED_LED 11
#define POT A1
#define NUMBER 4

#define MAX_PENALTIES 3
#define MAX_VALUE 255
#define MIN_VALUE 0

#define DEEP_SLEEP_TIME 10 * 1000 // in milliseconds
#define AFTER_GAME_END_TIME 10 * 1000
#define DEBOUNCE_TIME 200
#define TIME2 5000
#define TIME3 7000
#define REDUCTION_TIME_FACTOR 500

void gameReset();

void waitForStart();

void goSleeping();

void t1Pressed();

void buttonPressed();

void startGame();

void generatePattern();

void render();

void turnGreenLedsOff();

void listenButtons();

bool checkWin();

void assignPenalty();

void blinkLed(int pin);

bool checkDefeat();

long decreaseTime(long currentTime, int difficulty);
