def translate_en_us(cabinets, stairs, mosaics, LEDs, torches):
    JSON = """{
    "itemGroup.cabinets": "Cabinets",
    "itemGroup.innerOuterStairs": "Inner and Outer Stairs",
    "itemGroup.woodenMosaics": "Wooden Mosaics",
    "itemGroup.terracottaTiles": "Terracotta Tiles",
    "itemGroup.leds": "LEDs",
    "itemGroup.candlesticks": "Candlesticks",
    "itemGroup.humilityMisc": "Miscellaneous (Humility)",
    """

    for cabinet in cabinets:
        words = cabinet.split("_")

        # set first letter of each word to uppercase
        for i in range(len(words)):
            words[i] = words[i][0].upper() + words[i][1:]

        # merge words back together
        name = ""
        for word in words:
            name += word + " "

        JSON += """
    "block.humility-afm.cabinet_block_""" + cabinet + "\": \"" + name + """Cabinet",
    "block.humility-afm.illuminated_cabinet_block_""" + cabinet + "\": \"" + name + "Illuminated Cabinet\","

    for stairs in stairs:
        # print(stairs)
        words = stairs.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.inner_stairs_""" + stairs + "\": \"" + name + """Inner Stairs",
    "block.humility-afm.outer_stairs_""" + stairs + "\": \"" + name + "Outer Stairs\","

    for mosaic in mosaics:
        words = mosaic.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.wooden_mosaic_""" + mosaic + "\": \"" + name + "Wooden Mosaic\","

    for LED in LEDs:
        words = LED.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.led_""" + LED + "\": \"" + name + """LED\",
    "item.humility-afm.led_powder_""" + LED + "\": \"" + name + "LED Powder\","

    for torch in torches:
        words = torch.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.jack_o_lantern_""" + torch + "\": \"" + name + "Jack o'Lantern\","

    JSON = JSON[:-1]
    JSON += """
}"""

    return JSON


def translate_en_gb(json_text):
    return json_text.replace("Gray", "Grey")


def translate_pl_pl(cabinets, stairs, mosaics, LEDs, torches):
    JSON = """
{
    "itemGroup.cabinets": "Gabloty",
    "itemGroup.innerOuterStairs": "Schody wewnętrzne i zewnętrzne",
    "itemGroup.woodenMosaics": "Drewniane Mozaiki",
    "itemGroup.terracottaTiles": "Płytki z terakoty",
    "itemGroup.leds": "LED-y",
    "itemGroup.humilityMisc": "Różne (Humility)",
    """

    for cabinet in cabinets:
        words = cabinet.split("_")

        # set first letter of each word to uppercase
        for i in range(len(words)):
            words[i] = words[i][0].upper() + words[i][1:]

        # merge words back together
        name = ""
        for word in words:
            name += word + " "

        JSON += """
    "block.humility-afm.cabinet_block_""" + cabinet + "\": \"" + name + """Gablota",
    "block.humility-afm.illuminated_cabinet_block_""" + cabinet + "\": \"" + name + "Podświetlona Gablota\","

    for stairs in stairs:
        # print(stairs)
        words = stairs.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.inner_stairs_""" + stairs + "\": \"" + name + """Wewnętrzne Schody",
    "block.humility-afm.outer_stairs_""" + stairs + "\": \"" + name + "Zewnętrzne Schody\","

    for mosaic in mosaics:
        words = mosaic.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.wooden_mosaic_""" + mosaic + "\": \"" + name + "Drewniana Mozajka\","

    for LED in LEDs:
        words = LED.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.led_""" + LED + "\": \"" + name + """LED\",
    "item.humility-afm.led_powder_""" + LED + "\": \"" + name + "Proszek LED\","

    for torch in torches:
        words = torch.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        JSON += """
    "block.humility-afm.jack_o_lantern_""" + torch + "\": \"" + name + "Jack o'Lantern\","

    JSON = JSON[:-1]
    JSON += """
}"""
    return replace_polish_letters(JSON)


def replace_polish_letters(JSON):
    JSON = (JSON.replace("ą", "\\u0105")
            .replace("ć", "\\u0107").replace("ę", "\\u0119")
            .replace("ł", "\\u0142").replace("ń", "\\u0144")
            .replace("ó", "\\u00f3").replace("ś", "\\u015b")
            .replace("ź", "\\u017a").replace("ż", "\\u017c"))
    JSON = (JSON.replace("Ą", "\\u0104")
            .replace("Ć", "\\u0106").replace("Ę", "\\u0118")
            .replace("Ł", "\\u0141").replace("Ń", "\\u0143")
            .replace("Ó", "\\u00d3").replace("Ś", "\\u015a")
            .replace("Ź", "\\u0179").replace("Ż", "\\u017b"))
    return JSON
