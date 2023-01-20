//int i=0;
int cont2 = 0;
int lost = 1;
void call( boolean callFrom ){
  if (wifi){
  if(!client || !client.connected()){SOCKET= false;client.stop();return;}
  //Serial.print("Client "); Serial.print(client); Serial.print(" Client connected "); Serial.println(client.connected());
  if(callFrom){
    String Json = "{\"Status\":" + String(program_status) + ",\"Progress\":" + String(progress) + ",\"Maxtime\":" + String(max_time) + ",\"Message\":\"Chetan\",\"messageFor\":\"ping\"}";
    lost = client.println(Json);
    //   Serial.println(Json);
    //  delay(1000);
    long p = millis();
    while(millis()<p+1500){
      //Serial.print("cont2  ");Serial.print(cont2);
      if(client.available()){
        //Serial.print(" Client available "); Serial.println(client.available());
        String line = client.readStringUntil('\n');
        line.trim();
        if(line.length()>10){
          //   Serial.println( line );
          DeserializationError error = deserializeJson(jsonBuffer, line);
          if (error) {
            Serial.println("parseObject() failed");
          }
          else{

              //Serial.println("Values:");
              //Zahlen runden
              //((zahl+auf/2)/auf)*auf;
              single_duration_initial         =  float(jsonBuffer[String("singleshot")]);  single_duration_initial = ((single_duration_initial+100/2)/100)*100;//Serial.println(single_duration_initial);
              double_duration_initial          =  float(jsonBuffer[String("doubleshot")]); double_duration_initial = ((double_duration_initial+100/2)/100)*100;//Serial.println(double_duration_initial);
              pause_duration_initial          =  float(jsonBuffer[String("pause")]);       pause_duration_initial = ((pause_duration_initial+100/2)/100)*100;   //Serial.println(pause_duration_initial);
              watchdog_duration_initial       =  jsonBuffer[String("wait")];                //Serial.println(watchdog_duration);
              power_treshold_initial          =  jsonBuffer[String("trash")];               //Serial.println(power_treshold);
              double_shot_initial             =  jsonBuffer[String("mode")];                //Serial.println(double_shot);
              checkConfig();
              client.flush();
              return;
          }
        }
      client.flush();
      cont2=0;
    }
    else{
      /////////////////////////////////////////////////////////////////////////////////////////////////
      //////////////////This is the way to find the App is connected or not. //////////////////////////
      /////////////////////////////////////////////////////////////////////////////////////////////////
        if(cont2 == 0){
          cont2 = 1;Serial.println("Here1");return;
        }else if( cont2 == 1 ){
          cont2 = 2;Serial.println("Here2");return;
        }else if( cont2 == 2 ){
          cont2 = 3;Serial.println("Here3");return;
        }else if( cont2 == 3 ){
          cont2 = 4;Serial.println("Here4");return;
        }else if( cont2 == 4 ){cont2 = 0;Serial.println("Here5");SOCKET = false;return;
        }
      }
    } // end while loop
  }// end if call(true)
  else{ //call false
    String Json = "{\"Status\":" + String(program_status) + ",\"Progress\":" + String(progress) + ",\"Maxtime\":" + String(max_time) + ",\"Power\":"+power+",\"Message\":\"normal\",\"messageFor\":\"ping\"}";
    lost = client.println(Json);
  }
  //Serial.println(lost);
  if (lost == 0){
    client.stop();
    SOCKET = false;
    Serial.println("Client Lost");
  }
  }
}
