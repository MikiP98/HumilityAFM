import utils


woodTypes = [
    "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"
]


def generateAxeMineableJSON():
    from generateCabinetBlockVariantsJSONs import generateVanillaCabinetBlockVariants
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsWoodNames
    from generateWoodenMosaicVariantsJSONs import generateWoodenMosaicVariantsNames

    values = []

    for cabinet in generateVanillaCabinetBlockVariants():
        values.append(f"humility-afm:cabinet_block_{cabinet}")
        values.append(f"humility-afm:illuminated_cabinet_block_{cabinet}")

    for stairs in generateInnerOuterStairsVariantsWoodNames():
        values.append(f"humility-afm:inner_stairs_{stairs}")
        values.append(f"humility-afm:outer_stairs_{stairs}")

    for mosaic in generateWoodenMosaicVariantsNames():
        values.append(f"humility-afm:wooden_mosaic_{mosaic}")

    json = {
        "replace": False,
        "values": values
    }
    utils.save_json(json, "data/minecraft/tags/blocks/mineable/axe.json")


def generatePickaxeMineableJSON():
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsStoneNames
    from candlestrickScript import generateNames as generateCandlestickNames
    from generateTerracottaTilesVariantsJSONs import generateTerracottaTilesVariantsNames

    values = []

    for stairs in generateInnerOuterStairsVariantsStoneNames():
        values.append(f"humility-afm:inner_stairs_{stairs}")
        values.append(f"humility-afm:outer_stairs_{stairs}")

    for terracotta_tile in generateTerracottaTilesVariantsNames():
        values.append(f"humility-afm:{terracotta_tile}")

    for candlestick in generateCandlestickNames():
        values.append(f"humility-afm:{candlestick}")

    json = {
        "replace": False,
        "values": values
    }
    utils.save_json(json, "data/minecraft/tags/blocks/mineable/pickaxe.json")


def generateTranslationJSON():
    from generateCabinetBlockVariantsJSONs import generateCabinetBlockVariantsNames
    from generateInnerOuterStairsVariantsJSONs import generateInnerOuterStairsVariantsAllNames
    from generateWoodenMosaicVariantsJSONs import generateWoodenMosaicVariantsNames
    from generateLEDVariantsJSONs import generateLEDVariantsNames
    from pumpkinScript import torches
    import translationHelper

    cabinets = generateCabinetBlockVariantsNames()
    stairs = generateInnerOuterStairsVariantsAllNames()
    mosaics = generateWoodenMosaicVariantsNames()
    LEDs = generateLEDVariantsNames()

    # Generate en_us.json
    print("Generating en_us.json...")

    JSON = translationHelper.translate_en_us(cabinets, stairs, mosaics, LEDs, torches)
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
    JSON = translationHelper.translate_pl_pl(cabinets, stairs, mosaics, LEDs, torches)

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

    # print("Generating translation json...")
    # generateTranslationJSON()
    # print("Done!")
    ...
