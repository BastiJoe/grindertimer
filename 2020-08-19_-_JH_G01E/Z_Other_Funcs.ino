void readit(){
  // read the state of the switch into a local variable:
  reading = digitalRead(button);
  if (reading != lastButtonState) {
    // reset the debouncing timer
    lastDebounceTime = millis();
  }
}

void debounce(){
  if ((millis() - lastDebounceTime) > debounceDelay) {
    // whatever the reading is at, it's been there for longer than the debounce
    // delay, so take it as the actual current state:
    readit2();  
  }
}

void readit2(){
    // if the button state has changed:
    if (reading != buttonState) {
      buttonState = reading;
      toggle();      
    }
}

void toggle(){
  // only toggle the LED if the new button state is LOW
  if (buttonState == LOW) {
    wifi = !wifi;
    switchWifi();
    // set the LED:

  }  
}

void switchWifi(){
  if (wifi){
      Serial.print("Configuring access point...");
      //WiFi.softAP(ssid, password);
      WiFi.softAP(ssid);
      IPAddress myIP = WiFi.softAPIP();
      Serial.print("AP IP address: ");
      Serial.println(myIP);
      server.begin();
      //httpUpdater.setup(&httpServer);
      httpServer.begin();
      Serial.print("HTTPUpdateServer ready! Open http://192.168.4.1:81/update in your browser\n" );
      digitalWrite(LED_WiFi, LOW);
  }
  else{
      Serial.println("Closing Wifi");
      server.close();
      httpServer.close();
      WiFi.softAPdisconnect (true);
      digitalWrite(LED_WiFi, HIGH);
  }
}


//Energy meter with serial output
int getPower(){
  double activePower = 0;
  int power;
  
  // read CSE7766
  myCSE7766.handle();

  activePower = myCSE7766.getActivePower();
  power = (int) activePower;

  return power;
}



//Energy Meter with frequency ouput
/*
#define PULSE_TIMEOUT                   100000   //timeout after 100 ms = 100000 us

int getPower() {

  //simple Power detection
  
  int pulseHigh;
  int pulseLow;
  int pulseTotal;
  int frequency;
  int Power;
  //pulseIn(pin, value, timeout)
  pulseHigh = pulseIn(CF_PIN, HIGH, PULSE_TIMEOUT);
  pulseLow = pulseIn(CF_PIN, LOW, PULSE_TIMEOUT);

  pulseTotal = pulseHigh + pulseLow; // Time period of the pulse in microseconds

  if (pulseTotal == 0) {    //timeout
    return 0;               //return power = 0
  }
  else {
    frequency = 1000000 / pulseTotal; // Frequency in Hertz (Hz)
    Power = 1.7 * frequency;
    if (Power > 1000){
      Power = 0;
    }
    //return Power;
    return pulseTotal;
  }
  /*
  //Node Mcu v3
  return (digitalRead(powerpin)*150);

}
*/
