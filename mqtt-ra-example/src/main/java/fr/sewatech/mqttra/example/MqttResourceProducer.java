package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.MqttConnectionFactory;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;

public class MqttResourceProducer {

    @Produces @Resource(name="mqtt/AnswerCF")
    private MqttConnectionFactory answerConnectionFactory;

}
