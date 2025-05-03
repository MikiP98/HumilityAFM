import utils


terracottaTypes = [
    "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
    "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
]


def generateWoodenMosaicVariantsJSONs():
    for terracottaType in terracottaTypes:
        for terracottaType2 in terracottaTypes:
            if terracottaType == terracottaType2:
                continue

            print("Generating terracotta tiles jsons for " + terracottaType + " and " + terracottaType2 + "...")

            blockName = f"terracotta_tiles_{terracottaType}_{terracottaType2}"

            # Generate model json
            json = {
                "parent": "humility-afm:block/wooden_mosaic",
                "textures": {
                    "1": f"minecraft:block/{terracottaType}_terracotta",
                    "2": f"minecraft:block/{terracottaType2}_terracotta"
                }
            }

            # Write model json to file
            utils.save_json(json, f"assets/humility-afm/models/block/{blockName}.json")

            # Generate item model json
            json = {
                "parent": f"humility-afm:block/{blockName}"
            }

            # Write item model json to file
            utils.save_json(json, f"assets/humility-afm/models/item/{blockName}.json")

            # Generate blockstate json
            json = {
                "variants": {
                    "": {
                        "model": f"humility-afm:block/{blockName}"
                    }
                }
            }

            # Write blockstate json to file
            utils.save_json(json, f"assets/humility-afm/blockstates/{blockName}.json")

            # Generate recipes jsons
            pattern = [
                "AB",
                "BA"
            ]
            key = {
                "A": {
                    "item": f"minecraft:{terracottaType}_terracotta"
                },
                "B": {
                    "item": f"minecraft:{terracottaType2}_terracotta"
                }
            }
            result = {
                "item": f"humility-afm:{blockName}"
            }
            json = utils.generate_shaped_recipe(result, pattern, key)

            # Write recipe json to file
            utils.save_json(json, f"data/humility-afm/recipes/{blockName}.json")

            # Generate AB -> BA recipe json
            ingredient = {
                "item": f"humility-afm:{blockName}"
            }
            result = {
                "item": f"humility-afm:terracotta_tiles_{terracottaType2}_{terracottaType}"
            }
            json = utils.generate_shapeless_recipe(result, [ingredient])

            # Write recipe json to file
            utils.save_json(json, f"data/humility-afm/recipes/{blockName}_A_to_B.json")

            # Generate loot table json
            json = utils.generate_simple_loot_table(f"humility-afm:{blockName}")
            utils.save_json(json, f"data/humility-afm/loot_tables/blocks/{blockName}.json")


def generateTerracottaTilesVariantsNames():
    names = []
    for terracotta_type in terracottaTypes:
        for terracotta_type_2 in terracottaTypes:
            if terracotta_type == terracotta_type_2:
                continue
            names.append(f"terracotta_tiles_{terracotta_type}_{terracotta_type_2}")
    return names


generateWoodenMosaicVariantsJSONs()
