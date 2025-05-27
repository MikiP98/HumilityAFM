package io.github.mikip98.humilityafm.datagen.language.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TranslationHashMap extends PrefixedHashMap {

    public TranslationHashMap(String prefix) {
        super(prefix);
    }

    public String put(Block block, String value) {
        return super.putNoPrefix(block.getTranslationKey(), value);
    }
    public String put(Item item, String value) {
        return super.putNoPrefix(item.getTranslationKey(), value);
    }
}