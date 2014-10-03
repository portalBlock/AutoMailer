/*
 * Copyright (c) 2014 portalBlock. This work is provided AS-IS without any warranty.
 * You must provide a link back to the original project and clearly point out any changes made to this project.
 * This license must be included in all project files.
 * Any changes merged with this project are property of the copyright holder but may include the author's name.
 */

package net.portalblock.automailer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by portalBlock on 10/2/2014.
 */
@AllArgsConstructor
public class Email {

    @Getter
    private String fromAdr = "you@didnt.set", fromNme = "you-didnt-set", subject = "No Subject Set", body = "No message set!", cron = "";

    @Getter
    private List<String> to = new ArrayList<>();

    public static Email deserialize(JSONObject jsonObject){
        JSONArray toArr = jsonObject.getJSONArray("to");
        List<String> toAdr = new ArrayList<>();
        for(int i = 0; i < toArr.length(); i++){
            toAdr.add(toArr.getString(i));
        }
        return new Email(jsonObject.getString("fromAdr"),
                jsonObject.getString("fromNme"),
                jsonObject.getString("subject"),
                jsonObject.getString("body"),
                jsonObject.getString("cronTime"),
                toAdr
        );
    }

}
