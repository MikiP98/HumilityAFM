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
    import translationHelper

    itemGroups = ["cabinets_group", "inner_outer_stairs_group", "wooden_mosaics_group"]

    # Generate en_us.json
    print("Generating en_us.json...")

    JSON = translationHelper.translate_en_us(generateCabinetBlockVariantsNames(), generateInnerOuterStairsVariantsAllNames(), generateWoodenMosaicVariantsNames())
    orginal_JSON = JSON

    with open("src/main/resources/assets/humility-afm/lang/en_us.json", "w") as file:
        print("Saving en_us.json...")
        file.write(JSON)

    # Generate en_gb.json
    print("Generating en_gb.json...")

    JSON = translationHelper.translate_en_gb(orginal_JSON)

    with open("src/main/resources/assets/humility-afm/lang/en_gb.json", "w") as file:
        print("Saving en_gb.json...")
        file.write(JSON)

    # Generate pl_pl.json
    print("Generating pl_pl.json...")
    JSON = translationHelper.translate_pl_pl(generateCabinetBlockVariantsNames(), generateInnerOuterStairsVariantsAllNames(), generateWoodenMosaicVariantsNames())

    with open("src/main/resources/assets/humility-afm/lang/pl_pl.json", "w") as file:
        print("Saving pl_pl.json...")
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
