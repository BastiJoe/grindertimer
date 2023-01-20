      // Double Socket
      /*
      // DEFINES FOR Energy Meter
      #define CF_PIN                          5
      
      // Relays and Keys
      #define relay_1                     12
      #define relay_2                     15
      #define key_1                       13
      #define key_2                       4
      */
      
      // Single Socket Jenny, rechteckig
      /*
      #define CF_PIN                      4         //Energy Meter
      #define relay_1                     14        // Relay
      #define button                      1         //Button
      #define LED_power                   3         //LED 1
      #define LED_WiFi                    13        //LED 2
*/

            // Single Socket JH-G01E rund
      
      #define CF_PIN                      3         //Energy Meter
      #define relay_1                     14        // Relay
      #define button                      12         //Button
      //#define LED_power                   13         //LED 1        //no second LED in this plug
      #define LED_WiFi                    13        //LED 2           LED invers

      // NodeMcu V3

      /*
      #define powerpin                     16                 //D0
      #define relay_1                     LED_BUILTIN
      #define button                       5                  //D1
      #define LED_power                   0                   //D3
      #define LED_WiFi                    2                   //D4
*/



//Define Wifi Settings
#ifndef APSSID
#define APSSID "grindertimer"
#define APPSK  "grindertimer"
#endif
/* Set these to your desired credentials. */
const char *ssid = APSSID;
const char *password = APPSK;


// *********************************************************************
// define Variables
// *********************************************************************


// Parameters
byte check_parameter = 110;
byte check_parameter_adr = 0;

int single_duration_initial = 7000;  //in ms
int single_duration;                //in ms
byte single_duration_adr = 1;       //single shot duration adress in EEPROM = 1

int double_duration_initial = 10000;  //in ms
int double_duration;                //in ms
byte double_duration_adr = 3;       //double shot duration adress in EEPROM = 3

int pause_duration_initial = 4000;
int pause_duration;
byte pause_duration_adr = 5;

bool double_shot_initial = true;
bool double_shot;
byte double_shot_adr = 7;

int power_treshold_initial = 50;
int power_treshold;        //treshold from which the timer starts
byte power_treshold_adr = 9;

int watchdog_duration_initial = 1000;
int watchdog_duration;
byte watchdog_duration_adr = 11;
//Variables

//int relay_latency = 250;

byte program_status = 0;          // 0 Ready, 1 running single, 2 paused, 3 watchdog, 4 running double, 5 paused
byte prev_status = 0;               // 0 Ready, 1 running single, 2 paused, 3 watchdog, 4 running double, 5 paused

bool relay;
bool wifi = true;

int lastButtonState = LOW;   // the previous reading from the input pin
int buttonState;   // the previous reading from the input pin
int reading;
unsigned long lastDebounceTime = 0;  // the last time the output pin was toggled
unsigned long debounceDelay = 50; 

int progress = 0;
int max_time = 0;

int power;

int sent_update_interval = 200;          // time interval in ms for updating panel indicators // Waiting period
int receive_update_interval = 1000;

unsigned long startMillis;
unsigned long currentMillis;
unsigned long last_data_sent = 0;        // time of last update
unsigned long last_data_received = 0;        // time of last 

boolean WiFiConnectionState = false;
WiFiClient client;
boolean SOCKET = false;
