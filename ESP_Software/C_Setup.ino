void setup() {

  //Serial.begin(115200);

  //CSE7766 Energy Meter
  myCSE7766.setRX(3);
  myCSE7766.begin(); // will initialize serial to 4800 bps

  // put your setup code here, to run once:
  pinMode(relay_1, OUTPUT);
  //pinMode(LED_power, OUTPUT);   no second LED on PCB
  pinMode(LED_WiFi, OUTPUT);
  pinMode(button, INPUT);
  //pinMode(powerpin, INPUT);     // NodeMcu V3

//  hlw8012.begin(CF_PIN, CF1_PIN, SEL_PIN, CURRENT_MODE, false, 100000);
//  hlw8012.setResistors(CURRENT_RESISTOR, VOLTAGE_RESISTOR_UPSTREAM, VOLTAGE_RESISTOR_DOWNSTREAM);

  //load parameters from EEPROM
  initConfig();


  //close relays
  digitalWrite(relay_1, HIGH);
  //digitalWrite(LED_power, HIGH);
  digitalWrite(LED_WiFi, HIGH);

  //start Wifi
  /*
  Serial.print("Configuring access point...");
  //WiFi.softAP(ssid, password);
  WiFi.softAP(ssid);
  IPAddress myIP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  */
  
  //server.begin();
  httpUpdater.setup(&httpServer);
  //httpServer.begin();
  switchWifi();
  
}
