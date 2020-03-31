package com.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Play;
import com.twilio.twiml.voice.Say;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@RestController
public class TwilioController {

    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;

    @Value("${twilio.token}")
    public String AUTH_TOKEN;

    @Value("${to.phone.number}")
    public String TO_PHONE_NUMBER;

    @Value("${from.phone.number}")
    public String FROM_PHONE_NUMBER;

    @Autowired
    public RestTemplate restTemplate;

    public static final String UKRAINIAN_LANGUAGE = "Ukrainian";
    public static final String LANGUAGE_API_URL = "http://localhost:8080/language";
    public static final String UKRAINIAN_TEXT = "Слава Украини!";
    public static final String ENGLISH_TEXT = "GOD BLESS THE QUEEN!";
    public static final String URL_TO_NAVIGATE_CALL = "https://a093e1e6.ngrok.io/voice";

    @PostMapping(value = "/voice", produces = "text/xml")
    @ResponseBody
    public String generateTwiml() {

        HttpEntity<String> entity = restTemplate.exchange(LANGUAGE_API_URL, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        String language = entity.getBody();
        Say say;
        if (Objects.equals(language, UKRAINIAN_LANGUAGE)) {
            say = new Say.Builder(
                    UKRAINIAN_TEXT)
                    .language(Say.Language.RU_RU)
                    .build();
        } else {
            say = new Say.Builder(
                    ENGLISH_TEXT)
                    .language(Say.Language.EN_GB)
                    .build();
        }

        VoiceResponse voiceResponse = new VoiceResponse.Builder()
                .say(say)
                .build();

        return voiceResponse.toXml();
    }

    @PostMapping(value = "/make-call")
    @ResponseBody
    public String makeCall() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Call call = Call.creator(
                new com.twilio.type.PhoneNumber(TO_PHONE_NUMBER),
                new com.twilio.type.PhoneNumber(FROM_PHONE_NUMBER),
                URI.create(URL_TO_NAVIGATE_CALL))
                .create();

        return call.getSid();
    }

}
