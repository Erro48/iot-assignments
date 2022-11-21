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

/* CONSTANTS */
#define MAX_TASKS 50
#define MIN_LUM 650
#define TRIG_DELAY 3
#define WL1 500
#define WL2 800
#define WLMAX 1500
#define T1 3000

#define NORMAL_SAMPLING_PERIOD 100
#define PREALARM_SAMPLING_PERIOD 80
#define ALARM_SAMPLING_PERIOD 50

// minimal change that get the motor moving
#define MIN_MV 10
#define DEBOUNCE_TIME 200

#define POT_MIN_VALUE 0
#define POT_MAX_VALUE 1023

#define MOTOR_MIN_ALPHA 0
#define MOTOR_MAX_ALPHA 180


#define SCHEDULER_PERIOD 10
#define MONITOR_PERIOD 1000
#define TASK_PERIOD 100
#define T_LED_C_PERIOD 1000


#endif
