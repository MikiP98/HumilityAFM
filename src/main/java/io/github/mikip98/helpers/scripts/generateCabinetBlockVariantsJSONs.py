# coding=utf-8

import numpy

woodTypes = ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson", "warped", "bamboo"]
woolTypes = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

cabinetBlockVariantsNames = numpy.empty(len(woodTypes)*len(woolTypes), dtype='U19')

print("Generating cabinet block variants JSONs...")

def generateCabinetBlockVariants():
    i = 0
    for woodType in woodTypes:
        for woolType in woolTypes:
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            i += 1

def generateCabinetBlockVariantsJSONs(override = False):
    i = 0

    # ------------------------------------------------------------ WOOD TYPES LOOP ------------------------------------------------------------
    for woodType in woodTypes:
        # Create parent wood model JSON
        JSON = """{
    "parent": "humility-afm:block/cabinet_block",
    "textures": {
        "2": "minecraft:block/""" + woodType + """_planks",
        "particle": "minecraft:block/""" + woodType + """_planks"
    }
}"""
        # TODO If override is false, check if the file already exists if so, skip it, if not, create it
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + ".json", "w") as file:
            file.write(JSON)

        # Create parent wood open model JSON
        JSON = """{
    "parent": "humility-afm:block/cabinet_block_opened",
    "textures": {
        "2": "minecraft:block/""" + woodType + """_planks",
        "particle": "minecraft:block/""" + woodType + """_planks"
    }
}"""
        # TODO If override is false, check if the file already exists if so, skip it, if not, create it
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + ".json", "w") as file:
            file.write(JSON)


        # ------------------------------------------------------------ WOOL COLORS LOOP ------------------------------------------------------------
        for woolType in woolTypes:
            # Create cabinet block variant name
            # print("Generating cabinet block variant: " + woodType + "_" + woolType)
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            # print("Cabinet block variant name: " + cabinetBlockVariantsNames[i])
            # print("i: " + str(i))
            # print("Max i: " + str(len(woodTypes)*len(woolTypes) - 1))

            # Create direct block model JSON
            JSON = """{
    "parent": "humility-afm:block/cabinet_block_""" + woodType + """",
    "textures": {
        "1": "minecraft:block/""" + woolType + """_wool"
    }
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create direct open block model JSON
            JSON = """{
    "parent": "humility-afm:block/cabinet_block_opened_""" + woodType + """",
    "textures": {
        "1": "minecraft:block/""" + woolType + """_wool"
    }
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create item model JSON
            JSON = """{
    "parent": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """"
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/item/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = """{
    "multipart": [
        {
            "when": { "AND": [
                {"facing": "north" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 180 }
        },
        {
            "when": { "AND": [
                {"facing": "north" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 180 }
        },
        {
            "when": { "AND": [
                {"facing": "east" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 270 }
        },
        {
            "when": { "AND": [
                {"facing": "east" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 270 }
        },
        {
            "when": { "AND": [
                {"facing": "south" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """" }
        },
        {
            "when": { "AND": [
                {"facing": "south" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """" }
        },
        {
            "when": { "AND": [
                {"facing": "west" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 90 }
        },
        {
            "when": { "AND": [
                {"facing": "west" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 90 }
        }
    ]
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/blockstates/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = """{
    "type": "minecraft:crafting_shaped",
    "pattern": [
        " G ",
        "SWS",
        " S "
    ],
    "key": {
        "G": {
            "item": "minecraft:glass_pane"
        },
        "S": {
            "item": "minecraft:""" + woodType + """_slab"
        },
        "W": {
            "item": "minecraft:""" + woolType + """_carpet",
            "data": 0
        }
    },
    "result": {
        "item": "humility-afm:cabinet_block_""" + woodType + "_" + woolType + """"
    }
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/recipes/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create loot table JSON
            JSON = """{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "humility-afm:cabinet_block_""" + woodType + "_" + woolType + """"
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
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/loot_tables/blocks/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # ------------------------------------------------ ILLUMINATED CABINET VARIANTS ------------------------------------------------

            # Create item model JSON
            JSON = """{
    "parent": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """"
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/item/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = """{
    "multipart": [
        {
            "when": { "AND": [
                {"facing": "north" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 180 }
        },
        {
            "when": { "AND": [
                {"facing": "north" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 180 }
        },
        {
            "when": { "AND": [
                {"facing": "east" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 270 }
        },
        {
            "when": { "AND": [
                {"facing": "east" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 270 }
        },
        {
            "when": { "AND": [
                {"facing": "south" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """" }
        },
        {
            "when": { "AND": [
                {"facing": "south" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """" }
        },
        {
            "when": { "AND": [
                {"facing": "west" },
                {"open": true }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_opened_""" + woodType + "_" + woolType + """", "y": 90 }
        },
        {
            "when": { "AND": [
                {"facing": "west" },
                {"open": false }
            ]},
            "apply": { "model": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """", "y": 90 }
        }
    ]
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/blockstates/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = """{
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
        {
            "item": "minecraft:glow_ink_sac"
        },
        {
            "item": "humility-afm:cabinet_block_""" + woodType + "_" + woolType + """"
        }
    ],
    "result": {
        "item": "humility-afm:illuminated_cabinet_block_""" + woodType + "_" + woolType + """"
    }
}"""
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/recipes/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            i += 1

            # Create loot table JSON
            JSON = """{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "humility-afm:illuminated_cabinet_block_""" + woodType + "_" + woolType + """"
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
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/loot_tables/blocks/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

    print("Cabinet block variants JSONs generated!")

def createMinablesJSONs():
    createMineableAxeJSON()

def createMineableAxeJSON():
    JSON = """{
    "replace": false,
    "values": [
        "humility-afm:cabinet_block",
        "humility-afm:illuminated_cabinet_block\""""
    for cabinetBlockVariantName in cabinetBlockVariantsNames:
        JSON += ",\n\t\t\"humility-afm:cabinet_block_" + cabinetBlockVariantName + "\""
        JSON += ",\n\t\t\"humility-afm:illuminated_cabinet_block_" + cabinetBlockVariantName + "\""
    JSON += "\n\t]\n}"
    with open("src/main/resources/data/minecraft/tags/blocks/mineable/axe.json", "w") as file:
        file.write(JSON)


def generateCabinetBlockVariantsNames():
    generateCabinetBlockVariants()
    return cabinetBlockVariantsNames


if __name__ == "__main__":
    generateCabinetBlockVariantsJSONs()
    createMinablesJSONs()
