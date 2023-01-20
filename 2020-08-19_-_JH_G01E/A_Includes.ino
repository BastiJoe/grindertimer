// INCLUDES
#include <EEPROM.h>
#include <ESP8266WiFi.h> //
#include <WiFiClient.h> //
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h> //
#include <ESP8266HTTPUpdateServer.h>
#include <ArduinoJson.h>
#include "CSE7766.h"                              //https://github.com/ingeniuske/CSE7766

ESP8266WebServer httpServer(81);
WiFiServer server(80);
ESP8266HTTPUpdateServer httpUpdater;
String input;
DynamicJsonDocument  jsonBuffer(1024);



CSE7766 myCSE7766;


#include "HLW8012.h"
HLW8012 hlw8012;
