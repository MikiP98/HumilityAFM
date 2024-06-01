import utils


torches = ["redstone", "soul"]


def generateLEDVariantsJSONs():
    for color in torches:
        print(f"Generating Pumpkins variants JSON for {color}...")

        # Generate the model
        json = {
            "parent": "minecraft:block/orientable",
            "textures": {
                "front": f"humility-afm:block/jack_o_lantern_{color}",
                "side": "minecraft:block/pumpkin_side",
                "top": "minecraft:block/pumpkin_top"
            }
        }
        # Write the model to the file
        print("Writing model to file...")
        utils.save_json(json, f"assets/humility-afm/models/block/jack_o_lantern_{color}.json")

        # Generate the item model
        json = {
            "parent": f"humility-afm:block/jack_o_lantern_{color}"
        }
        # Write the item model to the file
        utils.save_json(json, f"assets/humility-afm/models/item/jack_o_lantern_{color}.json")

        # Generate the blockstate
        json = {
            "variants": {
                "facing=east": {"model": f"humility-afm:block/jack_o_lantern_{color}", "y": 90},
                "facing=north": {"model": f"humility-afm:block/jack_o_lantern_{color}"},
                "facing=south": {"model": f"humility-afm:block/jack_o_lantern_{color}", "y": 180},
                "facing=west": {"model": f"humility-afm:block/jack_o_lantern_{color}", "y": 270}
            }
        }
        # Write the blockstate to the file
        utils.save_json(json, f"assets/humility-afm/blockstates/jack_o_lantern_{color}.json")

        # Generate the recipe
        json = utils.generate_shapeless_recipe(
            f"humility-afm:jack_o_lantern_{color}",
            [
                f"minecraft:{color}_torch",
                "minecraft:carved_pumpkin"
            ]
        )
        # Write the recipe to the file
        utils.save_json(json, f"data/humility-afm/recipes/jack_o_lantern_{color}.json")

        # Generate the loot table
        json = utils.generate_simple_loot_table(f"humility-afm:jack_o_lantern_{color}")
        # Write the loot table to the file
        utils.save_json(json, f"data/humility-afm/loot_tables/blocks/jack_o_lantern_{color}.json")


if __name__ == "__main__":
    generateLEDVariantsJSONs()
