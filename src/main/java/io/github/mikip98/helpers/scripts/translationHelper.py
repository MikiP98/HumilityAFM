def translate_en_us(cabinets, stairs, mosaics):
    JSON = """{
    "itemGroup.cabinets": "Cabinets",
    "itemGroup.innerOuterStairs": "Inner and Outer Stairs",
    "itemGroup.woodenMosaics": "Wooden Mosaics",
    "itemGroup.terracottaTiles": "Terracotta Tiles",
    "itemGroup.leds": "LEDs",
    "itemGroup.humilityMisc": "Miscellaneous-Humility",
    """

    for cabinet in cabinets:
        # print(cabinet)
        wood, wool = cabinet.split("_", 1)
        wood = wood[0].upper() + wood[1:]
        wool = wool[0].upper() + wool[1:]

        # Double word wood check
        if wood == "Dark":
            wood_2, wool = wool.split("_", 1)
            wood = wood + " " + wood_2
            wool = wool[0].upper() + wool[1:]

        # Double word wool check
        # print(wool[:4])
        if wool[:5] == "Light":
            wool_2 = wool[6:]
            # print(wool_2)
            wool_2 = wool_2[0].upper() + wool_2[1:]
            wool = wool[:5] + " " + wool_2

        JSON += """
    "block.humility-afm.cabinet_block_""" + cabinet + "\": \"" + wood + " " + wool + """ Cabinet",
    "block.humility-afm.illuminated_cabinet_block_""" + cabinet + "\": \"" + wood + " " + wool + " Illuminated Cabinet\","

    for stairs in stairs:
        # print(stairs)
        words = stairs.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        name = name[:-1]
        JSON += """
    "block.humility-afm.inner_stairs_""" + stairs + "\": \"" + name + """ Inner Stairs",
    "block.humility-afm.outer_stairs_""" + stairs + "\": \"" + name + " Outer Stairs\","

    for mosaic in mosaics:
        words = mosaic.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        name = name[:-1]
        JSON += """
    "block.humility-afm.wooden_mosaic_""" + mosaic + "\": \"" + name + " Wooden Mosaic\","

    JSON = JSON[:-1]
    JSON += """
}"""

    return JSON

def translate_en_gb(json_text):
    return json_text.replace("Gray", "Grey")

def translate_pl_pl(cabinets, stairs, mosaics):
    JSON = """
{
    "itemGroup.cabinets": "Gabloty",
    "itemGroup.innerOuterStairs": "Schody wewnętrzne i zewnętrzne",
    "itemGroup.woodenMosaics": "Drewniane Mozaiki",
    "itemGroup.terracottaTiles": "Płytki z terakoty",
    "itemGroup.leds": "LEDy",
    "itemGroup.humilityMisc": "Różne-Humility",
    """

    for cabinet in cabinets:
        wood, wool = cabinet.split("_", 1)
        wood = wood[0].upper() + wood[1:]
        wool = wool[0].upper() + wool[1:]

        # Double word wood check
        if wood == "Dark":
            wood_2, wool = wool.split("_", 1)
            wood = wood + " " + wood_2
            wool = wool[0].upper() + wool[1:]

        # Double word wool check
        # print(wool[:4])
        if wool[:5] == "Light":
            wool_2 = wool[6:]
            # print(wool_2)
            wool_2 = wool_2[0].upper() + wool_2[1:]
            wool = wool[:5] + " " + wool_2

        JSON += """
    "block.humility-afm.cabinet_block_""" + cabinet + "\": \"" + wood + " " + wool + """ Cabinet",
    "block.humility-afm.illuminated_cabinet_block_""" + cabinet + "\": \"" + wood + " " + wool + " Illuminated Cabinet\","

    for stairs in stairs:
        # print(stairs)
        words = stairs.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        name = name[:-1]
        JSON += """
    "block.humility-afm.inner_stairs_""" + stairs + "\": \"" + name + """ Inner Stairs",
    "block.humility-afm.outer_stairs_""" + stairs + "\": \"" + name + " Outer Stairs\","

    for mosaic in mosaics:
        words = mosaic.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        name = name[:-1]
        JSON += """
    "block.humility-afm.wooden_mosaic_""" + mosaic + "\": \"" + name + " Wooden Mosaic\","

    JSON = JSON[:-1]
    JSON += """
}"""
    return JSON