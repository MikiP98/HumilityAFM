# coding=utf-8

import numpy
import utils

# from utils import Item, ShapelessRecipe, ShapedRecipe

vanilla_wood_types = ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson",
                      "warped", "bamboo"]
vanilla_wool_types = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray",
                      "cyan", "purple", "blue", "brown", "green", "red", "black"]

better_nether_wood_types = ["stalagnate", "willow", "wart", "mushroom_fir", "mushroom", "anchor_tree", "nether_sakura"]

cabinetBlockVariantsNames = numpy.empty(
    len(vanilla_wood_types) * len(vanilla_wool_types) + len(better_nether_wood_types) * len(vanilla_wool_types),
    dtype='U32')

print("Generating cabinet block variants JSONs...")


def generateVanillaCabinetBlockVariants():
    vanillaCabinetBlockVariantsNames = []
    for woodType in vanilla_wood_types:
        for woolType in vanilla_wool_types:
            vanillaCabinetBlockVariantsNames.append(woodType + '_' + woolType)
    return vanillaCabinetBlockVariantsNames


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
    JSON = {
        "parent": "humility-afm:block/cabinet_block",
        "textures": {
            "2": wood_mod + ":block/" + woodType + "_planks",
            "particle": wood_mod + ":block/" + woodType + "_planks"
        }
    }
    return JSON


def generate_parent_wood_open_model_JSON(woodType, wood_mod):
    JSON = {
        "parent": "humility-afm:block/cabinet_block_opened",
        "textures": {
            "2": wood_mod + ":block/" + woodType + "_planks",
            "particle": wood_mod + ":block/" + woodType + "_planks"
        }
    }
    return JSON


def generate_direct_block_model_JSON(woodType, woolType, wool_mod):
    JSON = {
        "parent": "humility-afm:block/cabinet_block_" + woodType,
        "textures": {
            "1": wool_mod + ":block/" + woolType + "_wool"
        }
    }
    return JSON


def generate_direct_open_block_model_JSON(woodType, woolType, wool_mod):
    JSON = {
        "parent": f"humility-afm:block/cabinet_block_opened_{woodType}",
        "textures": {
            "1": f"{wool_mod}:block/{woolType}_wool"
        }
    }
    return JSON


def generate_item_model_JSON(woodType, woolType):
    JSON = {
        "parent": f"humility-afm:block/cabinet_block_{woodType}_{woolType}"
    }
    return JSON


def generate_blockstate_JSON(woodType, woolType):
    directions = [("north", 180), ("east", 270), ("south", 0), ("west", 90)]
    multipart = []
    for direction in directions:
        multipart.append({
            "when": {"AND": [
                {"facing": direction[0]},
                {"open": True}
            ]},
            "apply": {"model": f"humility-afm:block/cabinet_block_opened_{woodType}_{woolType}", "y": direction[1]}
        })
        multipart.append({
            "when": {"AND": [
                {"facing": direction[0]},
                {"open": False}
            ]},
            "apply": {"model": f"humility-afm:block/cabinet_block_{woodType}_{woolType}", "y": direction[1]}
        })
    json = {
        "multipart": multipart
    }
    return json


def generate_recipe_JSON(woodType, woolType, wood_mod, wool_mod):
    pattern = [
        " G ",
        "SWS",
        " S "
    ]
    key = {
        "G": {
            "item": "minecraft:glass_pane"
        },
        "S": {
            "item": f"{wood_mod}:{woodType}_slab"
        },
        "W": {
            "item": f"{wool_mod}:{woolType}_carpet",
            "data": 0
        }
    }
    result = {
        "item": f"humility-afm:cabinet_block_{woodType}_{woolType}"
    }

    shaped_recipe = utils.generate_shaped_recipe(result, pattern, key)
    return shaped_recipe


def generate_illuminated_recipe_json(woodType, woolType):
    ingredients = [
        {"item": "minecraft:glow_ink_sac"},
        {"item": f"humility-afm:cabinet_block_{woodType}_{woolType}"}
    ]
    result = {"item": f"humility-afm:illuminated_cabinet_block_{woodType}_{woolType}"}
    shapeless_recipe = utils.generate_shapeless_recipe(result, ingredients)
    return shapeless_recipe


def generate_loot_table_JSON(woodType, woolType, illuminated=False):
    item = "humility-afm:" + ("illuminated_" if illuminated else '') + f"cabinet_block_{woodType}_{woolType}"
    JSON = utils.generate_simple_loot_table(item)
    return JSON


def generateCabinetBlockVariantsJSONs(override=False):
    i = 0

    # ------------------------------------------------ WOOD TYPES LOOP ------------------------------------------------
    for woodType in vanilla_wood_types:
        # Create parent wood model JSON
        JSON = generate_parent_wood_model_JSON(woodType, "minecraft")
        utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_{woodType}.json")

        # Create parent wood open model JSON
        JSON = generate_parent_wood_open_model_JSON(woodType, "minecraft")
        utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_opened_{woodType}.json")

        # ---------------------------------------------- WOOL COLORS LOOP ----------------------------------------------
        for woolType in vanilla_wool_types:
            # Create cabinet block variant name
            print("Generating cabinet block variant: " + woodType + "_" + woolType)
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            # print("Cabinet block variant name: " + cabinetBlockVariantsNames[i])
            # print("i: " + str(i))
            # print("Max i: " + str(len(woodTypes)*len(woolTypes) - 1))

            # Create direct block model JSON
            JSON = generate_direct_block_model_JSON(woodType, woolType, "minecraft")
            utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_{woodType}_{woolType}.json")

            # Create direct open block model JSON
            JSON = generate_direct_open_block_model_JSON(woodType, woolType, "minecraft")
            utils.save_json(
                JSON,
                f"assets/humility-afm/models/block/cabinet_block_opened_{woodType}_{woolType}.json"
            )

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            utils.save_json(JSON, f"assets/humility-afm/models/item/cabinet_block_{woodType}_{woolType}.json")

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            utils.save_json(JSON, f"assets/humility-afm/blockstates/cabinet_block_{woodType}_{woolType}.json")

            # Create recipe JSON
            JSON = generate_recipe_JSON(woodType, woolType, "minecraft", "minecraft")
            utils.save_json(JSON, "data/humility-afm/recipes/cabinet_block_" + woodType + "_" + woolType + ".json")

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType)
            utils.save_json(JSON, f"data/humility-afm/loot_tables/blocks/cabinet_block_{woodType}_{woolType}.json")

            # -------------------------------------- ILLUMINATED CABINET VARIANTS --------------------------------------

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            utils.save_json(
                JSON,
                f"assets/humility-afm/models/item/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            utils.save_json(
                JSON,
                f"assets/humility-afm/blockstates/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            # Create recipe JSON
            JSON = generate_illuminated_recipe_json(woodType, woolType)
            utils.save_json(
                JSON,
                f"data/humility-afm/recipes/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            i += 1

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType, True)
            utils.save_json(
                JSON,
                f"data/humility-afm/loot_tables/blocks/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

    # ----------------------------------------------- BN WOOD TYPES LOOP -----------------------------------------------
    for woodType in better_nether_wood_types:
        # Create parent wood model JSON
        JSON = generate_parent_wood_model_JSON(woodType, "betternether")
        utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_{woodType}.json")

        # Create parent wood open model JSON
        JSON = generate_parent_wood_open_model_JSON(woodType, "betternether")
        utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_opened_{woodType}.json")

        # ---------------------------------------------- WOOL COLORS LOOP ----------------------------------------------
        for woolType in vanilla_wool_types:
            print("Generating cabinet block variant: " + woodType + "_" + woolType)
            cabinetBlockVariantsNames[i] = (woodType + "_" + woolType)
            i += 1

            # Create direct block model JSON
            JSON = generate_direct_block_model_JSON(woodType, woolType, "minecraft")
            utils.save_json(JSON, f"assets/humility-afm/models/block/cabinet_block_{woodType}_{woolType}.json")

            # Create direct open block model JSON
            JSON = generate_direct_open_block_model_JSON(woodType, woolType, "minecraft")
            utils.save_json(
                JSON,
                f"assets/humility-afm/models/block/cabinet_block_opened_{woodType}_{woolType}.json"
            )

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            utils.save_json(JSON, f"assets/humility-afm/models/item/cabinet_block_{woodType}_{woolType}.json")

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            utils.save_json(JSON, f"assets/humility-afm/blockstates/cabinet_block_{woodType}_{woolType}.json")

            # Create recipe JSON
            JSON = generate_recipe_JSON(woodType, woolType, "betternether", "minecraft")
            utils.save_json(JSON, "data/humility-afm/recipes/cabinet_block_" + woodType + "_" + woolType + ".json")

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType)
            utils.save_json(
                JSON,
                "data/humility-afm/loot_tables/blocks/cabinet_block_" + woodType + "_" + woolType + ".json"
            )

            # -------------------------------------- ILLUMINATED CABINET VARIANTS --------------------------------------

            # Create item model JSON
            JSON = generate_item_model_JSON(woodType, woolType)
            utils.save_json(
                JSON,
                f"assets/humility-afm/models/item/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            # Create blockstate JSON
            JSON = generate_blockstate_JSON(woodType, woolType)
            utils.save_json(
                JSON,
                f"assets/humility-afm/blockstates/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            # Create recipe JSON
            JSON = generate_illuminated_recipe_json(woodType, woolType)
            utils.save_json(
                JSON,
                f"data/humility-afm/recipes/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

            # Create loot table JSON
            JSON = generate_loot_table_JSON(woodType, woolType, True)
            utils.save_json(
                JSON,
                f"data/humility-afm/loot_tables/blocks/illuminated_cabinet_block_{woodType}_{woolType}.json"
            )

    print("Cabinet block variants JSONs generated!")


def generateCabinetBlockVariantsNames():
    generateCabinetBlockVariants()
    return cabinetBlockVariantsNames


if __name__ == "__main__":
    generateCabinetBlockVariantsJSONs()
    # createMinablesJSONs()
