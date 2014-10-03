package net.portalblock.automailer;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by portalBlock on 10/2/2014.
 */
public class JSONConfig extends ArrayList<Email> {
    @Getter
    private String username, password, host;

    @Getter
    private int port;

    public void load(){
        JSONObject config = getConfigObject();
        if(config == null){
            System.out.println("Ending...");
            System.exit(0);
        }
        username = config.getString("username");
        password = config.getString("password");
        host = config.getString("host");
        port = config.getInt("port");

        loadEmails(config.getJSONArray("emails"));
    }

    private void loadEmails(JSONArray emails){
        for(int i = 0; i < emails.length(); i++){
            this.add(Email.deserialize(emails.getJSONObject(i)));
        }
    }

    private JSONObject getConfigObject(){
        File f = new File("config.json");
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String s;
            while ((s = reader.readLine()) != null){
                sb.append(s);
            }
            return new JSONObject(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            try{
                f.createNewFile();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

}
