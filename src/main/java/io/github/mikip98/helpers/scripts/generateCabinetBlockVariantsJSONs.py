# coding=utf-8

import numpy

vanilla_wood_types = ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson", "warped", "bamboo"]
vanilla_wool_types = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

better_nether_wood_types = ["stalagnate", "willow", "wart", "mushroom_fir", "mushroom", "anchor_tree", "nether_sakura"]

cabinetBlockVariantsNames = numpy.empty(len(vanilla_wood_types)*len(vanilla_wool_types) + len(better_nether_wood_types)*len(vanilla_wool_types), dtype='U32')

print("Generating cabinet block variants JSONs...")

def generateCabinetBlockVariants():
    i = 0
    for woodType in vanilla_wood_types:
        for woolType in vanilla_wool_types:
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            i += 1
    for woodType in better_nether_wood_types:
        for woolType in vanilla_wool_types:
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            i += 1

def generate_parent_wood_model_JSON(woodType, wood_mod):
    JSON = """{
    "parent": "humility-afm:block/cabinet_block",
    "textures": {
        "2": \"""" + wood_mod + ":block/" + woodType + """_planks",
        "particle": \"""" + wood_mod + ":block/" + woodType + """_planks"
    }
}"""
    return JSON
def generate_parent_wood_open_model_JSON(woodType, wood_mod):
    JSON = """{
    "parent": "humility-afm:block/cabinet_block_opened",
    "textures": {
        "2": \"""" + wood_mod + ":block/" + woodType + """_planks",
        "particle": \"""" + wood_mod + ":block/" + woodType + """_planks"
    }
}"""
    return JSON
def generate_direct_block_model_JSON(woodType, woolType, wool_mod):
    JSON = """{
    "parent": "humility-afm:block/cabinet_block_""" + woodType + """",
    "textures": {
        "1": \"""" + wool_mod + ":block/" + woolType + """_wool"
    }
}"""
    return JSON
def generate_direct_open_block_model_JSON(woodType, woolType, wool_mod):
    JSON = """{
    "parent": "humility-afm:block/cabinet_block_opened_""" + woodType + """",
    "textures": {
        "1": \"""" + wool_mod + ":block/" + woolType + """_wool"
    }
}"""
    return JSON
def generate_item_model_JSON(woodType, woolType):
    JSON = """{
    "parent": "humility-afm:block/cabinet_block_""" + woodType + "_" + woolType + """"
}"""
    return JSON
def generate_blockstate_JSON(woodType, woolType):
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
    return JSON
def generate_recipe_JSON(woodType, woolType, wood_mod, wool_mod):
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
            "item": \"""" + wood_mod + ':' + woodType + """_slab"
        },
        "W": {
            "item": \"""" + wool_mod + ':' + woolType + """_carpet",
            "data": 0
        }
    },
    "result": {
        "item": "humility-afm:cabinet_block_""" + woodType + "_" + woolType + """"
    }
}"""
    return JSON
def generate_illuminated_recipe_JSON(woodType, woolType):
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
    return JSON
def generate_loot_table_JSON(woodType, woolType, illuminated = False):
    JSON = """{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "humility-afm:""" + ("illuminated_" if illuminated else '') + "cabinet_block_" + woodType + '_' + woolType + """"
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
    return JSON

def generateCabinetBlockVariantsJSONs(override = False):
    i = 0

    # ------------------------------------------------------------ WOOD TYPES LOOP ------------------------------------------------------------
    for woodType in vanilla_wood_types:
        # Create parent wood model JSON
        JSON = generate_parent_wood_model_JSON(woodType, "minecraft")

        # TODO If override is false, check if the file already exists if so, skip it, if not, create it
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + ".json", "w") as file:
            file.write(JSON)

        # Create parent wood open model JSON
        JSON = generate_parent_wood_open_model_JSON(woodType, "minecraft")

        # TODO If override is false, check if the file already exists if so, skip it, if not, create it
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + ".json", "w") as file:
            file.write(JSON)


        # ------------------------------------------------------------ WOOL COLORS LOOP ------------------------------------------------------------
        for woolType in vanilla_wool_types:
            # Create cabinet block variant name
            print("Generating cabinet block variant: " + woodType + "_" + woolType)
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            # print("Cabinet block variant name: " + cabinetBlockVariantsNames[i])
            # print("i: " + str(i))
            # print("Max i: " + str(len(woodTypes)*len(woolTypes) - 1))

            # Create direct block model JSON
            JSON = generate_direct_block_model_JSON(woodType, woolType, "minecraft")
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create direct open block model JSON
            JSON = generate_direct_open_block_model_JSON(woodType, woolType, "minecraft")
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/item/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/blockstates/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = generate_recipe_JSON(woodType, woolType, "minecraft", "minecraft")
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/recipes/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/loot_tables/blocks/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # ------------------------------------------------ ILLUMINATED CABINET VARIANTS ------------------------------------------------

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/models/item/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/assets/humility-afm/blockstates/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = generate_illuminated_recipe_JSON(woodType, woolType)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/recipes/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            i += 1

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType, True)
            # TODO If override is false, check if the file already exists if so, skip it, if not, create it
            with open("src/main/resources/data/humility-afm/loot_tables/blocks/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

    # ------------------------------------------------------------ BN WOOD TYPES LOOP ------------------------------------------------------------
    for woodType in better_nether_wood_types:
        # Create parent wood model JSON
        JSON = generate_parent_wood_model_JSON(woodType, "betternether")
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + ".json", "w") as file:
            file.write(JSON)

        # Create parent wood open model JSON
        JSON = generate_parent_wood_open_model_JSON(woodType, "betternether")
        with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + ".json", "w") as file:
            file.write(JSON)

        # ------------------------------------------------------------ WOOL COLORS LOOP ------------------------------------------------------------
        for woolType in vanilla_wool_types:
            print("Generating cabinet block variant: " + woodType + "_" + woolType)
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            i += 1

            # Create direct block model JSON
            JSON = generate_direct_block_model_JSON(woodType, woolType, "minecraft")
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create direct open block model JSON
            JSON = generate_direct_open_block_model_JSON(woodType, woolType, "minecraft")
            with open("src/main/resources/assets/humility-afm/models/block/cabinet_block_opened_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            with open("src/main/resources/assets/humility-afm/models/item/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            with open("src/main/resources/assets/humility-afm/blockstates/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = generate_recipe_JSON(woodType, woolType, "betternether", "minecraft")
            with open("src/main/resources/data/humility-afm/recipes/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType)
            with open("src/main/resources/data/humility-afm/loot_tables/blocks/cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # ------------------------------------------------ ILLUMINATED CABINET VARIANTS ------------------------------------------------

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            with open("src/main/resources/assets/humility-afm/models/item/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            with open("src/main/resources/assets/humility-afm/blockstates/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create recipe JSON
            JSON = generate_illuminated_recipe_JSON(woodType, woolType)
            with open("src/main/resources/data/humility-afm/recipes/illuminated_cabinet_block_" + woodType + "_" + woolType + ".json", "w") as file:
                file.write(JSON)

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType, True)
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
