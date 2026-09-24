package io.github.mikip98.humilityafm.datagen.language.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TranslationHashMap extends PrefixedHashMap {

    public TranslationHashMap(String prefix) {
        super(prefix);
    }

    public String put(Block block, String value) {
        return super.putNoPrefix(block.getDescriptionId(), value);
    }
    public String put(Item item, String value) {
        return super.putNoPrefix(item.getDescriptionId(), value);
    }
}