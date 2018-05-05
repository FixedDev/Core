package us.sparknetwork.util;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

    private Pattern colorPattern = Pattern.compile("["+ ChatColor.COLOR_CHAR+"][0-9A-FK-OR]");

    private ComponentBuilder builder;

    public Text(String text){
        builder = new ComponentBuilder(text);
    }

    public Text(){
        builder = new ComponentBuilder("");
    }

    public Text append(String text){
        List<String> colors = new ArrayList<>();
        Matcher matcher = colorPattern.matcher(text);
        int index = 1;
        while(matcher.find()){
            colors.add(matcher.group(index));
            index++;
        }
        return this;
    }


    public void send(Player sender){

    }
}
