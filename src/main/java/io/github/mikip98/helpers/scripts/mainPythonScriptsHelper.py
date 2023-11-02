woodTypes = ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"]


def generateAxeMineableJSON():
    from generateCabinetBlockVariantsJSONs import generateCabinetBlockVariantsNames
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsWoodNames
    from generateWoodenMosaicVariantsJSONs import generateWoodenMosaicVariantsNames

    JSON = """{
    "replace": false,
    "values": ["""

    for cabinet in generateCabinetBlockVariantsNames():
        JSON += """
        "humility-afm:cabinet_block_""" + cabinet + """", 
        "humility-afm:illuminated_cabinet_block_""" + cabinet + """", """

    for stairs in generateInnerOuterStairsVariantsWoodNames():
        JSON += """
        "humility-afm:inner_stairs_""" + stairs + """", 
        "humility-afm:outer_stairs_""" + stairs + """", """

    for mosaic in generateWoodenMosaicVariantsNames():
        JSON += """
        "humility-afm:wooden_mosaic_""" + mosaic + """", """

    JSON = JSON[:-2]
    JSON += """
    ]
}"""

    with open("src/main/resources/data/minecraft/tags/blocks/mineable/axe.json", "w") as file:
        file.write(JSON)

def generatePickaxeMineableJSON():
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsStoneNames

    JSON = """{
    "replace": false,
    "values": ["""

    for stairs in generateInnerOuterStairsVariantsStoneNames():
        JSON += """
        "humility-afm:inner_stairs_""" + stairs + """",
        "humility-afm:outer_stairs_""" + stairs + """", """

    JSON = JSON[:-2]
    JSON += """
    ]
}"""

    with open("src/main/resources/data/minecraft/tags/blocks/mineable/pickaxe.json", "w") as file:
        file.write(JSON)

def generateTranslationJSON():
    from generateCabinetBlockVariantsJSONs import generateCabinetBlockVariantsNames
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsAllNames
    from generateWoodenMosaicVariantsJSONs import generateWoodenMosaicVariantsNames
    from translationHelper import translateWord

    itemGroups = ["cabinets_group", "inner_outer_stairs_group", "wooden_mosaics_group"]

    # Generate en_us.json
    JSON = """{
    "itemGroup.cabinets": "Cabinets",
    "itemGroup.innerOuterStairs": "Inner and Outer Stairs",
    "itemGroup.woodenMosaics": "Wooden Mosaics","""

    for cabinet in generateCabinetBlockVariantsNames():
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

    for stairs in generateInnerOuterStairsVariantsAllNames():
        # print(stairs)
        words = stairs.split("_")
        name = ""
        for word in words:
            name += word[0].upper() + word[1:] + " "
        name = name[:-1]
        JSON += """
    "block.humility-afm.inner_stairs_""" + stairs + "\": \"" + name + """ Inner Stairs",
    "block.humility-afm.outer_stairs_""" + stairs + "\": \"" + name + " Outer Stairs\","

    for mosaic in generateWoodenMosaicVariantsNames():
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

    with open("src/main/resources/assets/humility-afm/lang/en_us.json", "w") as file:
        file.write(JSON)

    # Generate pl_pl.json
    JSON = """{
    "itemGroup.cabinets": "Gabloty",
    "itemGroup.innerOuterStairs": "Schody wewnętrzne i zewnętrzne",
    "itemGroup.woodenMosaics": "Drewniane Mozaiki","""

    JSON += translate_pl_pl(generateCabinetBlockVariantsNames(), generateInnerOuterStairsVariantsAllNames(), generateWoodenMosaicVariantsNames())

    JSON = JSON[:-1]
    JSON += """
}"""

    with open("src/main/resources/assets/humility-afm/lang/pl_pl.json", "w") as file:
        file.write(JSON)


if __name__ == "__main__":
    # print("Generating mineable axe.json...")
    # generateAxeMineableJSON()
    # print("Done!")
    # print("Generating mineable pickaxe.json...")
    # generatePickaxeMineableJSON()
    # print("Done!")

    print("Generating translation json...")
    generateTranslationJSON()
    print("Done!")
