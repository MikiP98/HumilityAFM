metalTypes = {"gold", "copper", "exposed_copper", "weathered_copper", "oxidized_copper", "waxed_copper", "waxed_exposed_copper", "waxed_weathered_copper", "waxed_oxidized_copper"}

def generateNames():
    from generateCabinetBlockVariantsJSONs import vanilla_wool_types
    cabinetBlockVariantsNames = []
    for metalType in metalTypes:
        cabinetBlockVariantsNames.append("candlestick_" + metalType)
        cabinetBlockVariantsNames.append("candlestick_" + metalType + "_candle")
        for woolType in vanilla_wool_types:
            cabinetBlockVariantsNames.append("candlestick_" + metalType + "_candle_" + woolType)
    print(cabinetBlockVariantsNames)
    return cabinetBlockVariantsNames

def generateJSONs():
    # Generate candlestick blockstates
    placableNames = generateNames()
    for name in placableNames:
        JSON = """
{
    "multipart": [
        {
            "when": {"facing": "north" },
            "apply": { "model": "humility-afm:block/""" + name + """", "y": 180 }
        },
        {
            "when": {"facing": "east" },
            "apply": { "model": "humility-afm:block/""" + name + """", "y": 270 }
        },
        {
            "when": {"facing": "south" },
            "apply": { "model": "humility-afm:block/""" + name + """" }
        },
        {
            "when": {"facing": "west" },
            "apply": { "model": "humility-afm:block/""" + name + """", "y": 90 }
        }
    ]
}"""
        # import os
        # directory = "src/main/resources/assets/humility-afm/blockstates/"
        # os.makedirs(directory, exist_ok=True)
        print("src/main/resources/assets/humility-afm/blockstates/" + name + ".json")
        with open("src/main/resources/assets/humility-afm/blockstates/" + name + ".json", "w+") as file:
            file.write(JSON)

    # Generate candlestick models
    from generateCabinetBlockVariantsJSONs import vanilla_wool_types
    for metalType in metalTypes:
        JSON = """
{
    "parent": "humility-afm:block/candlestick",
    "textures": {
        "0": "block/""" + metalType + """_block",
        "particle": "block/""" + metalType + """_block"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + ".json", "w") as file:
            file.write(JSON)

        for woolType in vanilla_wool_types:
            JSON = """
{
    "parent": "humility-afm:block/candlestick_candle",
    "textures": {
        "0": "block/""" + metalType + """_block",
        "1": "block/""" + woolType + """_wool",
        "particle": "block/""" + metalType + """_block"
    }
}"""
            with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + "_candle_" + woolType + ".json", "w") as file:
                file.write(JSON)

    # Generate candlestick item models
    for metalType in metalTypes:
        JSON = """
{
    "parent": "humility-afm:block/candlestick_""" + metalType + """"
}"""
        with open("src/main/resources/assets/humility-afm/models/item/candlestick_" + metalType + ".json", "w") as file:
            file.write(JSON)

        JSON = """
{
    "parent": "humility-afm:block/candlestick_""" + metalType + """_candle"
}"""
        with open("src/main/resources/assets/humility-afm/models/item/candlestick_" + metalType + "_candle.json", "w") as file:
            file.write(JSON)

        for woolType in vanilla_wool_types:
            JSON = """
{
    "parent": "humility-afm:block/candlestick_""" + metalType + "_" + woolType + """"
}"""
            with open("src/main/resources/assets/humility-afm/models/item/candlestick_" + metalType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

    # Generate candlestick loot tables
    for name in placableNames:
        JSON = """
{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "humility-afm:""" + name + """
                }
            ],
            "conditions": [
                {
                    "condition": "minecraft:survives_explosion"
                }
            ]
        }
    ]
}"""
        with open("src/main/resources/data/humility-afm/loot_tables/blocks/" + name + ".json", "w") as file:
            file.write(JSON)


if __name__ == "__main__":
    generateJSONs()
