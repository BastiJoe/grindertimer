# grindertimer

## Used Hardware
I used JH_G01E smart plug with CSE7766 energy metering chip on ESP8266 basis.
The ESP8266 is flashed by using tuya convert.
https://github.com/ct-Open-Source/tuya-convert

<img src="https://images-na.ssl-images-amazon.com/images/I/61OKmS467nL._SL1500_.jpg" width="300">


## How it works
The coffee grinder is always powere (relay closed)
The energy metering chip on the smart socket detects the time, when the grinder starts (consumed power is rising high) and starts a timer.
When time is up, the relay opens and shuts down the grinder. After 5 seconds the relay closes again. This 5 seconds is used to take off the portafitler from the grinder.

The push button on the smart plug is to switch on and off the Wifi of the plug. Timing results are even better with switched off Wifi. Make sure to only switch on Wifi for configuration and shut it down for normal operation.

The Grindertimer has a single and a double shot mode.
Single shot mode is like explained above.

Double Shot mode: (for example single shot is set to 8 seconds, and double to 12)
- Portfilter is put to the grinder
- Grinder starts and runs for 8 seconds
- Relay opens for 5 Seconds
- Relay closes again and energy meter chip checks if the grinder starts again immediately (when Portafilter is still put to the grinder)
- If the grinder runs up immediately, the relay stays closed for the remaining 4 second
- Relay opens for 5 seconds

## App
The mobile Apps can be used to adjust the timers and the single/double shot mode


<img src="https://github.com/BastiJoe/grindertimer/blob/master/App_Screenshot.PNG" width="300">
