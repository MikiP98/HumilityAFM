pl_pl = {
}

def translateWord(word, language):
    if language == "pl_pl":
        fill_unknown_with_google = True

def translate_pl_pl(cabinets, stairs, mosaics):
    JSON = ""
    for cabinet in cabinets:
        wood, wool = cabinet.split("_", 1)
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

    return JSON[:-1]