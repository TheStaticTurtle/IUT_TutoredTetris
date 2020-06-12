package fr.iut.tetris;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.callbacks.DisconnectedCallback;
import net.arikia.dev.drpc.callbacks.ErroredCallback;

public class Discord {

    public Discord() {
        DiscordEventHandlers.Builder builder = new DiscordEventHandlers.Builder();

        builder.setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
        }).build();

        DiscordRPC.discordInitialize("721109970384453683",builder.build(),true);

        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }).start();
        //setIngameStatus(46,"SOLO");
        setInMenuStatus();
    }

    static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public void setIngameStatus(int scoreA,int scoreB,String mode){
        if(mode.equals("VERSUS")) {

            boolean isLegacy = Config.getInstance().getBool("LEGACY_PIECES");
            DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("PlayerA: "+scoreA+"  PlayerB: "+scoreB);
            if(isLegacy) {
                builder.setDetails(capitalize(mode)+" (Legacy)");
                builder.setBigImage("icon","Tetris (Legacy)");
            } else {
                builder.setDetails(capitalize(mode)+" (Normal)");
                builder.setBigImage("icon","Tetris (Normal)");
            }

            DiscordRPC.discordUpdatePresence(builder.build());
        } else {
            boolean isLegacy = Config.getInstance().getBool("LEGACY_PIECES");
            int max = Config.getInstance().getInt("SCORE_"+mode+"_BEST");
            if(isLegacy) {
                max = Config.getInstance().getInt("SCORE_"+mode+"_BEST_LEGACY");
            }
            DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("Score: "+scoreA+" (Max:"+max+")");
            if(isLegacy) {
                builder.setDetails(capitalize(mode)+" (Legacy)");
                builder.setBigImage("icon","Tetris (Legacy)");
            } else {
                builder.setDetails(capitalize(mode)+" (Normal)");
                builder.setBigImage("icon","Tetris (Normal)");
            }

            DiscordRPC.discordUpdatePresence(builder.build());
        }
    }

    public void setInMenuStatus() {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("In menu");
        boolean isLegacy = Config.getInstance().getBool("LEGACY_PIECES");

        if(isLegacy) {
            builder.setBigImage("icon","Tetris (Legacy)");
        } else {
            builder.setBigImage("icon","Tetris (Normal)");
        }
        DiscordRPC.discordUpdatePresence(builder.build());
    }
}
