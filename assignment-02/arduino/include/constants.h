#ifndef __CONSTANTS__
#define __CONSTANTS__

/* PINS */
/* -Sensors */
#define P_SONAR_TRIG 7
#define P_SONAR_ECHO 8
#define P_PIR 9
#define P_BUTTON 11
#define P_PHOTORESISTOR A0
#define P_POT A1

/* -Actuators */
#define P_LED_A 3
#define P_LED_B 4
#define P_LED_C 5
#define P_MOTOR 10


/* PERIODS */
#define SCHEDULER_PERIOD 10
#define MONITOR_PERIOD 1000
#define TASK_PERIOD 100
#define LED_C_PERIOD 1000

#define NORMAL_SAMPLING_PERIOD 100
#define PREALARM_SAMPLING_PERIOD 80
#define ALARM_SAMPLING_PERIOD 50


/* CONSTANTS */
/* -General */
#define MAX_TASKS 50
#define MIN_LUM 650
#define TRIG_DELAY 3
#define RIVERBED_LEVEL 2500
#define WL1 1400
#define WL2 700
#define WLMAX 250
#define T1 3000
// minimal change that get the motor moving
#define MIN_MOTOR_MV 10
#define DEBOUNCE_TIME 200
#define SOUND_SPEED  (331.45 + 0.62*20)

/* -Ranges */
#define POT_MIN_VALUE 0
#define POT_MAX_VALUE 1023
#define MOTOR_MIN_ALPHA 0
#define MOTOR_MAX_ALPHA 180

/* -Lcd */
#define LCD_ADDRESS 0x27
#define LCD_COLS 20
#define LCD_ROWS 4

#endif
