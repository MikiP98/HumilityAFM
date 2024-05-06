metalTypes = {"gold", "copper", "exposed_copper", "weathered_copper", "oxidized_copper", "waxed_copper", "waxed_exposed_copper", "waxed_weathered_copper", "waxed_oxidized_copper"}

def generateNames() -> list[str]:
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
    placableNames: list[str] = generateNames()
    for name in placableNames:
        words = frozenset(name.split("_"))
        if "candle" in words:
            JSON = """
    {
        "variants": {
            "facing=north,lit=false": { "model": "humility-afm:block/""" + name + """", "y": 180 },
            "facing=east,lit=false": { "model": "humility-afm:block/""" + name + """", "y": 270 },
            "facing=south,lit=false": { "model": "humility-afm:block/""" + name + """" },
            "facing=west,lit=false": { "model": "humility-afm:block/""" + name + """", "y": 90 },
            
            "facing=north,lit=true": { "model": "humility-afm:block/""" + name + """_lit", "y": 180 },
            "facing=east,lit=true": { "model": "humility-afm:block/""" + name + """_lit", "y": 270 },
            "facing=south,lit=true": { "model": "humility-afm:block/""" + name + """_lit" },
            "facing=west,lit=true": { "model": "humility-afm:block/""" + name + """_lit", "y": 90 }
        }
    }"""
        else:
            JSON = """
    {
        "variants": {
            "facing=north": { "model": "humility-afm:block/""" + name + """", "y": 180 },
            "facing=east": { "model": "humility-afm:block/""" + name + """", "y": 270 },
            "facing=south": { "model": "humility-afm:block/""" + name + """" },
            "facing=west": { "model": "humility-afm:block/""" + name + """", "y": 90 }
        }
    }"""

        # directory = "src/main/resources/assets/humility-afm/blockstates/"
        # os.makedirs(directory, exist_ok=True)
        print("src/main/resources/assets/humility-afm/blockstates/" + name + ".json")
        with open("src/main/resources/assets/humility-afm/blockstates/" + name + ".json", "w+") as file:
            file.write(JSON)

    # Generate candlestick models
    from generateCabinetBlockVariantsJSONs import vanilla_wool_types
    for metalType in metalTypes:
        is_block = ""
        if metalType == "gold" or metalType == "copper" or metalType == "waxed_copper":
            is_block = "_block"
        metal_texture = metalType.replace("waxed_", "")
        JSON = """
{
    "parent": "humility-afm:block/candlestick",
    "textures": {
        "0": "block/""" + metal_texture + is_block + """",
        "particle": "block/""" + metal_texture + is_block + """"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + ".json", "w") as file:
            file.write(JSON)

        JSON = """
{
    "parent": "humility-afm:block/candlestick_candle",
    "textures": {
        "0": "block/""" + metal_texture + is_block + """",
        "2": "block/candle",
        "particle": "block/""" + metal_texture + is_block + """"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + "_candle.json", "w") as file:
            file.write(JSON)

        JSON = """
{
    "parent": "humility-afm:block/candlestick_candle",
    "textures": {
        "0": "block/""" + metal_texture + is_block + """",
        "2": "block/candle_lit",
        "particle": "block/""" + metal_texture + is_block + """"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + "_candle_lit.json", "w") as file:
            file.write(JSON)

        for woolType in vanilla_wool_types:
            JSON = """
{
    "parent": "humility-afm:block/candlestick_candle",
    "textures": {
        "0": "block/""" + metal_texture + is_block + """",
        "2": "block/""" + woolType + """_candle",
        "particle": "block/""" + metal_texture + is_block + """"
    }
}"""
            with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + "_candle_" + woolType + ".json", "w") as file:
                file.write(JSON)

        for woolType in vanilla_wool_types:
            JSON = """
{
    "parent": "humility-afm:block/candlestick_candle",
    "textures": {
        "0": "block/""" + metal_texture + is_block + """",
        "2": "block/""" + woolType + """_candle_lit",
        "particle": "block/""" + metal_texture + is_block + """"
    }
}"""
            with open("src/main/resources/assets/humility-afm/models/block/candlestick_" + metalType + "_candle_" + woolType + "_lit.json", "w") as file:
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
