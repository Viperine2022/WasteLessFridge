
//Librairie pour la conexion d'une carte ESP8266 au wifi
#include <ESP8266WiFi.h>

//Librairie pour pouvoir executes des sons personalisés dans le hautparleur
#include <pitches.h>

//Librairie pour la création et gestion d'un client MQTT (publish/subscribe sur des topics)
#include <PubSubClient.h>

//Pin relié au TERMOMETRE
#define TMPPin A0

#ifndef STASSID
#define STASSID "ssid wifi"
#define STAPSK  "password wifi"
#endif

//Wifi access credentials 
const char* ssid     = STASSID;
const char* password = STAPSK;

//Pin relié au haut parleur
int port_beeper = 14;

//Durée en ms de la note qui se reproduit dans le hautparleur
int noteDuration = 500;

//Valeur de la temperature mesurée
double temp;

//Mesure si la temperature est en dessus de la valeur de reference mesuré pendant le setup 
boolean temp_ok = true;

//Intensité de la lumière à l'intérieur du frigo
double lux;

//Mesure si la lumière est en dessus d'une valeur de reference fixe 
boolean lux_ok = true;

//Max quantité de lumière admissible à l'intérieur d'un frigo fermé 
boolean lux_ref = 70;

//Nombre de fois que l'alarme est répéte avant de s'étaeindre 
int nb_iter_alarme = 0;

//Temperature de réference à l'intérieur du refrigerateur qui se messure lors de la phase de setup 
double temp_ref;


// MQTT Broker Elements 

//Host du serveur MQTT 
const char *mqtt_broker = "9ec58e2d6f204debb3f2169d403acdae.s1.eu.hivemq.cloud";
//Topic dans lequel les messages du senseur IOT seront publiés et lues du côte application mobile
const char *topic = "BIP";
//Nom d'utilisateur du client MQTT 
const char *mqtt_username = "frigo1";
//Mot de passe du client MQTT 
const char *mqtt_password = "PASSword1";
//Port de conexion avec protocole TLS 
const int mqtt_port = 8883;

//Client MQTT generé à partir de la session wifi établie 
WiFiClient espClient;
PubSubClient client(espClient);



void setup() {
  //Serial.begin(115200);
  pinMode(LED_BUILTIN, OUTPUT);
  
  //Serial.println("wifi");

  //établissement du type de conexion wifi et début de la conexion
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  
  //Si la connexion echoue on reessaie de se connecte toutes les demies-secondes jusqu'à réusir 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  
   //Serial.println("wifi_connected");
  // Wifi connected = 2 blink
  digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on 
  delay(1000);                      // Wait for a second
  digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off
  delay(1000);  
  digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on 
  delay(1000);                      // Wait for a second
  digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off

  //Attendre 5 secondes pour pouvoir fermer la porte du frigo dès que le wifi es connecté
  delay(5000); 

  //Messure de la temperature de réference telle que si la temperature remonte au dessus de ce seuil l'alarme est declenché niveau application mobile
  temp_ref = read_temp();
  //Serial.println("temp ref ok");

//Ici on fait connexion avec le serveur MQTT mais en pratique on avait tout le temps un echec dans la conexion avec le code -4 et nous n'avons pas 
//réussi à résoudre ce problème. 
/**

  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);
  while (!client.connected()) {
      String client_id = "esp8266-client-";
      client_id += String(WiFi.macAddress());

      if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {

      } else {
         digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on 
         delay(1000);                      // Wait for a second
         digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off
         delay(1000);
      }
  }
  */


  // Mélodie qui s'execute quand la phase de setup est finie. 
  // Melody Zelda secret
  tone(port_beeper, NOTE_G5, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_FS5, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_DS5, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_A4, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_GS4, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_E5, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_GS5, noteDuration*0.3); // setup ended
  delay(noteDuration*0.3);
  tone(port_beeper, NOTE_C6, noteDuration*0.3); // setup ended

  //envoyer un message au telephone avec la temperature de reference avec frigo fermé
  //ainsi l'appli mobile pourra détecter des cas d'alarme à partir du flux de données. 
  //client.publish(topic = "temperature_reference", "%d", temp_ref);
  
}

//Fonction qui retourne la temperature à l'intérieur du frigo en degrés celsuis
double read_temp()
{
  int sensorInput = analogRead(TMPPin);
  double temp = (double)sensorInput * (3300/1024) ; //find percentage of input reading
  temp = (temp - 500) / 10; //multiply by 5V to get voltage
  return temp;
}

/*Ceci est une methode fictive qui prendrait la messure de la lumière à l'intérieur du frigo 
afin de détecter si la porte serait ouverte ou pas mais ceci ne peut pas Être fait avec notre 
capteur lumière qui est un détecteur de couleur (il émet lui même de la lumière)*/
double read_lux(){}

void loop() {
  //Est vrai si la temperature ou la luminosité est trop haute
  boolean alarme = false;
  
  //On commence par lire la temperature 
  temp = read_temp();
  lux = read_lux();

  //envoyer un message au telephone pour l'historique de temperature et luminosité
  //client.publish(topic, "temperature historique");
  //client.publish(topic, "lumiosite historique");


  // SI la température ou la luminosité est trop haute alors alarme est assigné à true
  if ((temp > temp_ref+4)||(lux > lux_ref + 10 )){
    
    nb_iter_alarme += 1;
    alarme = true;
  } else {
    nb_iter_alarme = 0;
    alarme = false;
  }

  //Si c'est la première fois qu'un temperature ou luminosité trop haute est detecté le dispositif ne beepera pas 
  //ceci est fait pour donner un certain délai à l'utilisateur pour ranger ses courses sans des bruits genants de la part de la carte. 
  if ( alarme && nb_iter_alarme < 5 && nb_iter_alarme != 1) {
    
    for (int i=0;i<10;i++){

      tone(port_beeper, NOTE_G4, noteDuration);

      delay(noteDuration*2);

    }

  }

  //On espace les périodes de beeps de plus en plus et finalement on arrête de beeper au bout de 5 itérations parce que si c'est le cas 

  if (!alarme || nb_iter_alarme > 5) {
    delay(180); // si ok loop toute les 30 minutes A RECHANGER
  } else {
    delay(12000*nb_iter_alarme); // sinon loop toute les 2 minutes * nb_itération
  }
}
