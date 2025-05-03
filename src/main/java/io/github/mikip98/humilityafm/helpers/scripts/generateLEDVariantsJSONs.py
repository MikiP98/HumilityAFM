import utils


colors = [
    "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue",
    "purple", "magenta", "pink", "brown", "black", "gray", "light_gray", "white"
]


def generateLEDVariantsNames():
    return colors


def generateLEDVariantsJSONs():
    print("Generating LED variants JSONs...")
    for color in colors:
        print(f"Generating LED variants JSON for {color}...")

        # Generate the powder item model
        json = {
            "parent": "item/generated",
            "textures": {
                "layer0": f"humility-afm:item/led_powder_{color}"
            }
        }
        # Write the powder item model to the file
        utils.save_json(json, f"assets/humility-afm/models/item/led_powder_{color}.json")

        # Generate the powder item recipe
        ingredients = [
            {
                "item": "minecraft:glowstone_dust"
            },
            {
                "item": "minecraft:redstone",
                "data": 0
            },
            {
                "item": f"minecraft:{color}_dye",
                "data": 0
            }
        ]
        output = {
            "item": f"humility-afm:led_powder_{color}",
            "count": 1
        }
        json = utils.generate_shapeless_recipe(output, ingredients)
        # Write the powder item recipe to the file
        utils.save_json(json, f"data/humility-afm/recipes/led_powder_{color}.json")

        # Generate the model
        json = {
            "parent": "humility-afm:block/led",
            "textures": {
                "0": f"block/{color}_concrete",
                "particle": f"block/{color}_terracotta"
            }
        }
        # Write the model to the file
        utils.save_json(json, f"assets/humility-afm/models/block/led_{color}.json")

        # Generate the inner model
        json = {
            "parent": "humility-afm:block/led_inner",
            "textures": {
                "0": f"block/{color}_concrete",
                "particle": f"block/{color}_terracotta"
            }
        }
        # Write the inner model to the file
        utils.save_json(json, f"assets/humility-afm/models/block/led_inner_{color}.json")

        # Generate the outer model
        json = {
            "parent": "humility-afm:block/led_outer",
            "textures": {
                "0": f"block/{color}_concrete",
                "particle": f"block/{color}_terracotta"
            }
        }
        # Write the outer model to the file
        utils.save_json(json, f"assets/humility-afm/models/block/led_outer_{color}.json")

        # Generate the item model
        json = {
            "parent": f"humility-afm:block/led_{color}"
        }

        # Write the item model to the file
        utils.save_json(json, f"assets/humility-afm/models/item/led_{color}.json")

        # Generate the blockstate
        # directions = ["north", "east", "south", "west"]
        # halfs = [("bottom", 0), ("top", 180)]  # X rotation
        # shapes = [
        #     ("inner_left", (0, 270, 180, 90)),
        #     ("inner_right", ),
        #     ("outer_left", ),
        #     ("outer_right", ),
        #     ("straight", )
        # ]
        # variants = {}
        # for direction in directions:
        #     for half, x in halfs:
        #         ...
        json = {
            "variants": {
                "facing=east,half=bottom,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "uvlock": True
                },
                "facing=east,half=bottom,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 90,
                    "uvlock": True
                },
                "facing=east,half=bottom,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "uvlock": True
                },
                "facing=east,half=bottom,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 90,
                    "uvlock": True
                },
                "facing=east,half=bottom,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "x": 180,
                    "y": 90,
                    "uvlock": True
                },
                "facing=east,half=top,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 270,
                    "uvlock": True
                },
                "facing=east,half=top,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}"
                },
                "facing=east,half=top,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 270,
                    "uvlock": True
                },
                "facing=east,half=top,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}"
                },
                "facing=east,half=top,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "y": 270,
                    "uvlock": True
                },
                "facing=north,half=bottom,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 270,
                    "uvlock": True
                },
                "facing=north,half=bottom,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "uvlock": True
                },
                "facing=north,half=bottom,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 270,
                    "uvlock": True
                },
                "facing=north,half=bottom,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "uvlock": True
                },
                "facing=north,half=bottom,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "x": 180,
                    "uvlock": True
                },
                "facing=north,half=top,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 180,
                    "uvlock": True
                },
                "facing=north,half=top,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 270,
                    "uvlock": True
                },
                "facing=north,half=top,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 180,
                    "uvlock": True
                },
                "facing=north,half=top,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 270,
                    "uvlock": True
                },
                "facing=north,half=top,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "y": 180,
                    "uvlock": True
                },
                "facing=south,half=bottom,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 90,
                    "uvlock": True
                },
                "facing=south,half=bottom,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 180,
                    "uvlock": True
                },
                "facing=south,half=bottom,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 90,
                    "uvlock": True
                },
                "facing=south,half=bottom,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 180,
                    "uvlock": True
                },
                "facing=south,half=bottom,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "x": 180,
                    "y": 180,
                    "uvlock": True
                },
                "facing=south,half=top,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}"
                },
                "facing=south,half=top,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 90,
                    "uvlock": True
                },
                "facing=south,half=top,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}"
                },
                "facing=south,half=top,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 90,
                    "uvlock": True
                },
                "facing=south,half=top,shape=straight": {
                    "model": f"humility-afm:block/led_{color}"
                },
                "facing=west,half=bottom,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 180,
                    "uvlock": True
                },
                "facing=west,half=bottom,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "x": 180,
                    "y": 270,
                    "uvlock": True
                },
                "facing=west,half=bottom,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 180,
                    "uvlock": True
                },
                "facing=west,half=bottom,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "x": 180,
                    "y": 270,
                    "uvlock": True
                },
                "facing=west,half=bottom,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "x": 180,
                    "y": 270,
                    "uvlock": True
                },
                "facing=west,half=top,shape=inner_left": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 90,
                    "uvlock": True
                },
                "facing=west,half=top,shape=inner_right": {
                    "model": f"humility-afm:block/led_inner_{color}",
                    "y": 180,
                    "uvlock": True
                },
                "facing=west,half=top,shape=outer_left": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 90,
                    "uvlock": True
                },
                "facing=west,half=top,shape=outer_right": {
                    "model": f"humility-afm:block/led_outer_{color}",
                    "y": 180,
                    "uvlock": True
                },
                "facing=west,half=top,shape=straight": {
                    "model": f"humility-afm:block/led_{color}",
                    "y": 90,
                    "uvlock": True
                }
            }
        }
        # json = {
        #     "variants": variants
        # }
        # Write the blockstate to the file
        utils.save_json(json, f"assets/humility-afm/blockstates/led_{color}.json")

        # Generate the recipe
        json = {
            "type": "minecraft:crafting_shaped",
            "pattern": [
                "QQQ",
                "PPP",
                "GGG"
            ],
            "key": {
                "Q": {
                    "item": "minecraft:quartz"
                },
                "P": {
                    "item": f"humility-afm:led_powder_{color}"
                },
                "G": {
                    "item": "minecraft:glass_pane"
                }
            },
            "result": {
                "item": f"humility-afm:led_{color}"
            }
        }
        # Write the recipe to the file
        utils.save_json(json, f"data/humility-afm/recipes/led_{color}.json")

        # Generate the loot table
        json = utils.generate_simple_loot_table(f"humility-afm:led_{color}")
        # Write the loot table to the file
        utils.save_json(json, f"data/humility-afm/loot_tables/blocks/led_{color}.json")


if __name__ == "__main__":
    generateLEDVariantsJSONs()
