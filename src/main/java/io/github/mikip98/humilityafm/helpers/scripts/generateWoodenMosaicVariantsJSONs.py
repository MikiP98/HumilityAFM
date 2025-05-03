import utils


from mainPythonScriptsHelper import woodTypes


def generateWoodenMosaicVariantsJSONs():
    for woodType in woodTypes:
        for woodType2 in woodTypes:
            if woodType == woodType2:
                continue

            print("Generating wooden mosaic jsons for " + woodType + " and " + woodType2 + "...")

            # Generate model json
            JSON = {
                "parent": "humility-afm:block/wooden_mosaic",
                "textures": {
                    "1": f"minecraft:block/{woodType}_planks",
                    "2": f"minecraft:block/{woodType2}_planks"
                }
            }
            # Write model json to file
            fileName = f"wooden_mosaic_{woodType}_{woodType2}.json"
            utils.save_json(JSON, f"assets/humility-afm/models/block/{fileName}")

            # Generate item model json
            JSON = {
                "parent": f"humility-afm:block/wooden_mosaic_{woodType}_{woodType2}"
            }
            # Write item model json to file
            utils.save_json(JSON, f"assets/humility-afm/models/item/{fileName}")

            # Generate blockstate json
            JSON = {
                "variants": {
                    "": {
                        "model": f"humility-afm:block/wooden_mosaic_{woodType}_{woodType2}"
                    }
                }
            }
            # Write blockstate json to file
            utils.save_json(JSON, f"assets/humility-afm/blockstates/{fileName}")

            # Generate recipes jsons
            pattern = [
                "AB",
                "BA"
            ]
            key = {
                "A": {
                    "item": f"minecraft:{woodType}_planks"
                },
                "B": {
                    "item": f"minecraft:{woodType2}_planks"
                }
            }
            result = {
                "item": f"humility-afm:wooden_mosaic_{woodType}_{woodType2}"
            }
            JSON = utils.generate_shaped_recipe(result, pattern, key)
            # Write recipe json to file
            fileName = "wooden_mosaic_" + woodType + "_" + woodType2 + ".json"
            utils.save_json(JSON, f"data/humility-afm/recipes/{fileName}")

            # Generate AB -> BA recipe json
            result = {
                "item": f"humility-afm:wooden_mosaic_{woodType2}_{woodType}"
            }
            ingredients = [{
                "item": f"humility-afm:wooden_mosaic_{woodType}_{woodType2}"
            }]
            JSON = utils.generate_shapeless_recipe(result, ingredients)

            # Write recipe json to file
            fileName = "wooden_mosaic_" + woodType2 + "_" + woodType + "_A_to_B.json"
            utils.save_json(JSON, f"data/humility-afm/recipes/{fileName}")

            # Generate loot tables jsons
            JSON = utils.generate_simple_loot_table(f"humility-afm:wooden_mosaic_{woodType}_{woodType2}")
            # Write loot table json to file
            utils.save_json(JSON, f"data/humility-afm/loot_tables/blocks/wooden_mosaic_{woodType}_{woodType2}.json")


def generateWoodenMosaicVariantsNames():
    names = []
    for woodType in woodTypes:
        for woodType2 in woodTypes:
            if woodType == woodType2:
                continue
            names.append(woodType + "_" + woodType2)
    return names


generateWoodenMosaicVariantsJSONs()
