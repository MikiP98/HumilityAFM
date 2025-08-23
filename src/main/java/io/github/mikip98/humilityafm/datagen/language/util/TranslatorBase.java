package io.github.mikip98.humilityafm.datagen.language.util;

import io.github.mikip98.humilityafm.util.generation_data.material_management.data_types.BlockMaterial;

import java.util.Iterator;

public abstract class TranslatorBase {
    protected TranslationHashMap translations;

    protected TranslatorBase(TranslationHashMap translationsMap) {
        this.translations = translationsMap;
    }

    protected void enterTranslations(
            Iterable<BlockMaterial> materials,
            String idPrefix,
            String nameSuffix
    ) {
        for (BlockMaterial material : materials) {
            final String id = idPrefix + material.getSafeName();
            final String name = formatName(material.getRawName()) + nameSuffix;
            translations.put(id, name);
        }
    }

    protected static String formatName(String name) {
        String[] words = name.split("_");
        Iterator<String> it = java.util.Arrays.stream(words).iterator();

        StringBuilder formattedName = new StringBuilder();
        String firstWord = it.next();
        formattedName.append(Character.toUpperCase(firstWord.charAt(0)))
                .append(firstWord.substring(1).toLowerCase())
                .append(" ");

        while (it.hasNext()) {
            String word = it.next();
            formattedName.append(word.toLowerCase()).append(" ");
        }
        return formattedName.toString().trim();
    }
}
