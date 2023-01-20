void initConfig() {
  //Check if parameters have ever been stored to EEPROM
  //If not, store default values to EEPROM
  EEPROM.begin(16);
  Serial.println("");
  Serial.print("EEPROM Check parameter : "); Serial.println(EEPROM.read(check_parameter_adr));
  if (EEPROM.read(check_parameter_adr) != check_parameter) {
    Serial.println("Initial Parameters not set. Setting them to default values");
    updateConfig();
  }

  //Load values from EEPROM
  loadConfig();
  EEPROM.end();
}


void loadConfig(){
  // Loads configuration from EEPROM into RAM
  EEPROM.begin(16);
  Serial.println("Loading Parameters from EEPROM");

  // SINGLE
  single_duration = eepromReadInt(single_duration_adr);
  Serial.print("Single : "); Serial.println(single_duration);

  //DOUBLE
  double_duration = eepromReadInt(double_duration_adr);
  Serial.print("Double : "); Serial.println(double_duration);

  //PAUSE
  pause_duration = eepromReadInt(pause_duration_adr);
  Serial.print("Pause : "); Serial.println(pause_duration);

  //Watchdog
  watchdog_duration = eepromReadInt(watchdog_duration_adr);
  Serial.print("Watchdog_Duration : "); Serial.println(watchdog_duration);

  //Treshold
  power_treshold = eepromReadInt(power_treshold_adr);
  Serial.print("power_treshold : "); Serial.println(power_treshold);

  //BOOL Doube
  double_shot = eepromReadInt(double_shot_adr);
  Serial.print("Double Mode : "); Serial.println(double_shot);
  EEPROM.end();
}

void checkConfig() {

  //Check if any value has been changed
  bool outdated = false;

  if (single_duration_initial <= 1000) {single_duration_initial = 1000;};
  if (double_duration_initial <= 2000) {double_duration_initial = 2000;};
  if (pause_duration_initial <= 4000) {pause_duration_initial = 4000;};
  if (watchdog_duration_initial <= 1000) {watchdog_duration_initial = 1000;};
  if (power_treshold_initial <= 25) {power_treshold_initial = 25;};
  
  if (single_duration_initial   !=  single_duration)    {outdated = true;};
  if (double_duration_initial   !=  double_duration)    {outdated = true;};
  if (pause_duration_initial    !=  pause_duration)     {outdated = true;};
  if (watchdog_duration_initial !=  watchdog_duration)  {outdated = true;};
  if (power_treshold_initial    !=  power_treshold)     {outdated = true;};
  if (double_shot_initial       !=  double_shot)        {outdated = true;};

  //Update changed values in EEPROM
  if (outdated){
    Serial.println("Parameters Updated. Writing them to EEPROM");
    updateConfig();
    loadConfig();
  }
}


void updateConfig() {

  //Write *_initial vlues to EEPROM
  //*_initial values can be default values on very first startup or Values that are received by APP
  EEPROM.begin(16);
  Serial.println("WRITING EEPROM!!!");
  // SINGLE
  Serial.print("Single : "); Serial.println(single_duration_initial);
  eepromWriteInt(single_duration_adr, single_duration_initial);

  //DOUBLE
  Serial.print("Double : "); Serial.println(double_duration_initial);
  eepromWriteInt(double_duration_adr, double_duration_initial);

  //PAUSE
  Serial.print("Pause : "); Serial.println(pause_duration_initial);
  eepromWriteInt(pause_duration_adr, pause_duration_initial);

  //Watchdog
  Serial.print("Watchdog : "); Serial.println(watchdog_duration_initial);
  eepromWriteInt(watchdog_duration_adr, watchdog_duration_initial);

  //Treshold
  Serial.print("Treshold : "); Serial.println(power_treshold_initial);
  eepromWriteInt(power_treshold_adr, power_treshold_initial);

  //BOOL Doube
  Serial.print("Double Mode : "); Serial.println(double_shot_initial);
  eepromWriteInt(double_shot_adr, double_shot_initial);
 


  EEPROM.write(check_parameter_adr, check_parameter);

  EEPROM.commit();                      // Only needed for ESP8266 to get data written
  EEPROM.end();                         // Free RAM copy of structure
}


void eepromWriteInt(int adr, int value) {
  byte low, high;
  low = value & 0xFF;
  high = (value >> 8) & 0xFF;
  EEPROM.write(adr, low); // dauert 3,3ms
  EEPROM.write(adr + 1, high);

  return;
} //eepromWriteInt


int eepromReadInt(int adr) {
  byte low, high;
  low = EEPROM.read(adr);
  high = EEPROM.read(adr + 1);
  return low + ((high << 8) & 0xFF00);
} //eepromReadInt
