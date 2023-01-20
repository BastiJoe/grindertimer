void loop() {
  if(wifi){
    if (!SOCKET) client = server.available();
    if (client.connected() && SOCKET == false) {
      Serial.println("Connected");
      client.println("SOCKET_CONNECTED");
      delay(200);  //delay(500);
      client.println("SOCKET_VERSION"); //PACKET HEADER
      delay(200);
      client.println("{\"HW\":\"JH-G01E\",\"FW\":\"V02\"}");
      delay(200);
      String d;
      if(double_shot == 0){d = "false";}else{d = "true";}
      client.println("SOCKET_PING");
      delay(200);
      client.println(     "{\"single\":" + String(single_duration) + ",\"double\":" + String(double_duration) + ",\"power\":" + power_treshold + ",\"waiting\":"+ watchdog_duration +",\"pause\":"+ pause_duration +",\"shot\":\""+ d +"\",\"messageFor\":\"connected\"}"       );
      Serial.println(     "{\"single\":" + String(single_duration) + ",\"double\":" + String(double_duration) + ",\"power\":" + power_treshold + ",\"waiting\":"+ watchdog_duration +",\"pause\":"+ pause_duration +",\"shot\":\""+ d +"\",\"messageFor\":\"connected\"}"       );
      delay(200); //delay(1000);
      SOCKET  =  true;
    }
    httpServer.handleClient();
  }
  // *********************************************************************
  // Timer Program
  // *********************************************************************
  
  currentMillis = millis();
  power = getPower();

  readit();
  debounce();
  // save the reading. Next time through the loop, it'll be the lastButtonState:
  lastButtonState = reading;

  prev_status = program_status;               // 0 Ready, 1 running single, 2 paused, 3 watchdog, 4 running double, 5 paused

  switch (prev_status)
  {
    case 0: //Ready
      if (power >= power_treshold)
      {
        program_status = 1; //running single
        startMillis = currentMillis;
            Serial.println("Grinder started");
            Serial.println("Running Single");
      }
      else
      {
        if ((millis() - last_data_received) > receive_update_interval) {
          last_data_received = millis();
          if (!client || !client.connected()) {
            SOCKET = false;
            client.stop();

          }
            if (SOCKET){  call(true);  //Serial.println("Receiving");
          }
        }
      }
      progress = 0;
      //max_time = 0;
      relay = HIGH;
      break;
    case 1: //running single
      if ((currentMillis - startMillis) >= single_duration)
      {
        program_status = 2;
        Serial.println("Finished Single");
        Serial.println("Pause 1");
      }
      progress = (((currentMillis - startMillis) * 100) / single_duration);
      //max_time = single_duration;
      relay = HIGH;
      break;
    case 2: //Pause 1
      if ((currentMillis - startMillis) >= (single_duration + pause_duration))
      {
        if (double_shot)
        {
          program_status = 3; // watchdog for double shot
          Serial.println("Watching Double");
        }
        else
        {
          program_status = 0; //Ready
          Serial.println("Double Disabled");
          Serial.println("Reset Grinder");
        }
      }
      progress = 0;
      //max_time = single_duration;
      relay = LOW;
      break;
    case 3: //watchdog
      if (power >= power_treshold)
      {
        program_status = 4; //running double
        Serial.println("Running Double");
        startMillis = currentMillis;
      }
      if ((currentMillis - startMillis) >= (single_duration + pause_duration + watchdog_duration))
      {
        program_status = 0; //Ready
        Serial.println("No Double");
      }
      progress = 0;
      //max_time = 0;
      //write to ANDROID APP: status , Progress , max Time
      relay = HIGH;
      break;
    case 4: //Running double
      if ((currentMillis - startMillis) >= double_duration - single_duration)
      {
        program_status = 5;
        Serial.println("Finished Double");
        Serial.println("Pause 2");
      }
      progress = (((currentMillis - startMillis + single_duration) * 100) / double_duration);
      //max_time = double_duration;
      relay = HIGH;
      break;
    case 5: //Pause 2
      if ((currentMillis - startMillis) >= (double_duration - single_duration + pause_duration))
      {
        program_status = 0; //Ready
        Serial.println("Reset Grinder");
      }
      progress = 0;
      //max_time = 0;
      relay = LOW;
      break;
    default:
      break;
  }

  if ((millis() - last_data_sent) > sent_update_interval)
  {
    last_data_sent = millis();
    //Update Android APP
    /*
        Serial.print("Status : "); Serial.print(program_status);
        Serial.print("     Progress : "); Serial.print(progress);
        Serial.print("     Power : "); Serial.println(power);
        */
    if (!client || !client.connected()) {
      SOCKET = false;
      client.stop();
    }
     //Serial.println("Seing");
    if (SOCKET) {
      //Serial.println("Sending");
      call(false);
    }
  }
  digitalWrite(relay_1, relay);
  //digitalWrite(LED_power, relay);
}
