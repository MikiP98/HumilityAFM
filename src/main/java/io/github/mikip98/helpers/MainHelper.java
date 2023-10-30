package io.github.mikip98.helpers;

import lombok.Getter;

import java.util.Arrays;

public class MainHelper {
    public MainHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse static methods and variables instead!");
    }

    public static final String[] vanillaWoodTypes = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson", "warped", "bamboo"};
    public static final String[] vanillaWoolTypes = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

    //@Getter
    public static String[] woodTypes = vanillaWoodTypes;
    //@Getter
    public static String[] woolTypes = vanillaWoolTypes;
    public static String[] getWoodTypes() {return woodTypes;}
    public static String[] getWoolTypes() {return woolTypes;}


    public static void addWoodType(String newWoodType) {
        woodTypes = Arrays.copyOf(woodTypes, woodTypes.length + 1);
        woodTypes[woodTypes.length - 1] = newWoodType;
    }
    public static void addWoodTypes(String[] newWoodTypes) {
        woodTypes = Arrays.copyOf(woodTypes, woodTypes.length + newWoodTypes.length);
        System.arraycopy(newWoodTypes, 0, woodTypes, woodTypes.length - newWoodTypes.length, newWoodTypes.length);
    }


    public static void addWoolType(String newWoolType) {
        woolTypes = Arrays.copyOf(woolTypes, woolTypes.length + 1);
        woolTypes[woolTypes.length - 1] = newWoolType;
    }
    public static void addWoolTypes(String[] newWoolTypes) {
        woolTypes = Arrays.copyOf(woolTypes, woolTypes.length + newWoolTypes.length);
        System.arraycopy(newWoolTypes, 0, woolTypes, woolTypes.length - newWoolTypes.length, newWoolTypes.length);
    }
}
