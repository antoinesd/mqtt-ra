# Getting Started

Build the project and run Tomee+

    mvn clean install
    mvn tomee:run

It will run Tomee+ with an embedded MQTT broker listening on port 1883. It deploys the MQTT resource adapter and an example application.

# Play with the example

The example application has 2 MDBs, listening on the swt/Question topic and publishing back on the swt/Answer topic.

Use a mqtt client and

1. subscribe to the swt/Answer topic,
2. publish a text message in the swt/Question topic.

You'll see some print in stdout and a "OK" message in the answer topic.

# MQTT Client

What, you don't have a MQTT client ? You could [install Mosquitto](http://www.jtips.info/index.php?title=MQTT/Mosquitto) (bah, it's in french).
It comes with the command line mosquitto_pub and mosquitto_sub clients.

What, you don't wanna install this great command line tool ? You're probably not a backend guy/girl. OK, I'll give you some HTML/JS. Hopefully you
have a modern browser, then it should work because Tomee+ supports MQTT + WebSockets. The magic page is at the [root of the example application]
(http://localhost:8080/mqtt-ra-example-1.0-SNAPSHOT/).

How does it work ?

1. Connect
2. Subscriptions : add New Topic Subscription : swt/Answer
3. Publish : on topic swt/Question
