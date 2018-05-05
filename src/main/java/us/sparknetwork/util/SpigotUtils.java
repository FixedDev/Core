package us.sparknetwork.util;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BLACK;
import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.LIGHT_PURPLE;
import static org.bukkit.ChatColor.MAGIC;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.STRIKETHROUGH;
import static org.bukkit.ChatColor.UNDERLINE;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.ChatColor;

public class SpigotUtils {
    public static net.md_5.bungee.api.ChatColor toBungee(final ChatColor color) {
        switch (color) {
            case BLACK: {
                return net.md_5.bungee.api.ChatColor.BLACK;
            }
            case DARK_BLUE: {
                return net.md_5.bungee.api.ChatColor.DARK_BLUE;
            }
            case DARK_GREEN: {
                return net.md_5.bungee.api.ChatColor.DARK_GREEN;
            }
            case DARK_AQUA: {
                return net.md_5.bungee.api.ChatColor.DARK_AQUA;
            }
            case DARK_RED: {
                return net.md_5.bungee.api.ChatColor.DARK_RED;
            }
            case DARK_PURPLE: {
                return net.md_5.bungee.api.ChatColor.DARK_PURPLE;
            }
            case GOLD: {
                return net.md_5.bungee.api.ChatColor.GOLD;
            }
            case GRAY: {
                return net.md_5.bungee.api.ChatColor.GRAY;
            }
            case DARK_GRAY: {
                return net.md_5.bungee.api.ChatColor.DARK_GRAY;
            }
            case BLUE: {
                return net.md_5.bungee.api.ChatColor.BLUE;
            }
            case GREEN: {
                return net.md_5.bungee.api.ChatColor.GREEN;
            }
            case AQUA: {
                return net.md_5.bungee.api.ChatColor.AQUA;
            }
            case RED: {
                return net.md_5.bungee.api.ChatColor.RED;
            }
            case LIGHT_PURPLE: {
                return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
            }
            case YELLOW: {
                return net.md_5.bungee.api.ChatColor.YELLOW;
            }
            case WHITE: {
                return net.md_5.bungee.api.ChatColor.WHITE;
            }
            case MAGIC: {
                return net.md_5.bungee.api.ChatColor.MAGIC;
            }
            case BOLD: {
                return net.md_5.bungee.api.ChatColor.BOLD;
            }
            case STRIKETHROUGH: {
                return net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
            }
            case UNDERLINE: {
                return net.md_5.bungee.api.ChatColor.UNDERLINE;
            }
            case ITALIC: {
                return net.md_5.bungee.api.ChatColor.ITALIC;
            }
            case RESET: {
                return net.md_5.bungee.api.ChatColor.RESET;
            }
            default: {
                throw new IllegalArgumentException("Unrecognised Bukkit colour " + color.name() + ".");
            }
        }
    }

    public static ChatColor toBukkit(final net.md_5.bungee.api.ChatColor color) {
        switch (color) {
            case BLACK: {
                return BLACK;
            }
            case DARK_BLUE: {
                return DARK_BLUE;
            }
            case DARK_GREEN: {
                return DARK_GREEN;
            }
            case DARK_AQUA: {
                return DARK_AQUA;
            }
            case DARK_RED: {
                return DARK_RED;
            }
            case DARK_PURPLE: {
                return DARK_PURPLE;
            }
            case GOLD: {
                return GOLD;
            }
            case GRAY: {
                return GRAY;
            }
            case DARK_GRAY: {
                return DARK_GRAY;
            }
            case BLUE: {
                return BLUE;
            }
            case GREEN: {
                return GREEN;
            }
            case AQUA: {
                return AQUA;
            }
            case RED: {
                return RED;
            }
            case LIGHT_PURPLE: {
                return LIGHT_PURPLE;
            }
            case YELLOW: {
                return YELLOW;
            }
            case WHITE: {
                return WHITE;
            }
            case MAGIC: {
                return MAGIC;
            }
            case BOLD: {
                return BOLD;
            }
            case STRIKETHROUGH: {
                return STRIKETHROUGH;
            }
            case UNDERLINE: {
                return UNDERLINE;
            }
            case ITALIC: {
                return ITALIC;
            }
            case RESET: {
                return RESET;
            }
            default: {
                throw new IllegalArgumentException("Unrecognised Bukkit colour " + color.name() + ".");
            }
        }
    }


}
