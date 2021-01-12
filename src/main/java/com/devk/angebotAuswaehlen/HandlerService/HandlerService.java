package com.devk.angebotAuswaehlen.HandlerService;

import com.devk.angebotAuswaehlen.model.Angebot;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class HandlerService implements ExternalTaskHandler {


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        try {
            Map<String, Object> bestesAngebot = new HashMap<>();
            ArrayList<Angebot> offerList = externalTask.getVariable("AngebotsListe");
            log.info("Liste von dreiBesteAngeboten wird eingeholt: " + offerList.toString());

            // Bei keinem Angebot -> Fehler
            if(offerList.isEmpty()) {
                externalTaskService.handleFailure(externalTask, externalTask.getId(), "Es wurde kein Angebot zum Auswerten empfangen", 0, 1000L);
            }
            // Bei einem Angebot -> genau das
            else if(offerList.size() == 1) {
                bestesAngebot.put("bestesAngebot", offerList.get(0));
                externalTaskService.complete(externalTask, bestesAngebot);
            }
            else {
                // Bei mehr als einem Angebot
                Angebot ausgewaehltesAngebot = Collections.min(offerList, Comparator.comparing(Angebot::getGesamtpreis));
                bestesAngebot.put("bestesAngebot",ausgewaehltesAngebot);
                log.info("Das beste Angebot ist: " + ausgewaehltesAngebot.toString());
                externalTaskService.complete(externalTask, bestesAngebot);
            }
        } catch (Exception e) {
            log.error("Fehler: ", e);
            externalTaskService.handleFailure(externalTask, externalTask.getId(), e.getMessage(), 1, 100L);
        }
    }
}
