# grindertimer

## Used Hardware

I used JH_G01E smart plug with energy metering chip on ESP8266 basis.
The ESP8266 is flashed by using tuya convert.
https://github.com/ct-Open-Source/tuya-convert

## How it works

The coffee grinder is always powere (relay closed)
The energy metering chip on the smart socket detects the time, when the grinder starts (consumed power is rising high) and starts a timer.
When time is up, the relay opens and shuts down the grinder. After 5 seconds the relay closes again.
