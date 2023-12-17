torches = ["redstone", "soul"]

def generateLEDVariantsJSONs():
    for color in torches:
        print(f"Generating Pumpkins variants JSON for {color}...")

        # Generate the model
        JSON = """{
    "parent": "minecraft:block/orientable",
    "textures": {
        "front": "humility-afm:block/jack_o_lantern_""" + color + """",
        "side": "minecraft:block/pumpkin_side",
        "top": "minecraft:block/pumpkin_top"
    }
}"""
        # Write the model to the file
        with open(f"src/main/resources/assets/humility-afm/models/block/jack_o_lantern_{color}.json", "w") as f:
            print(f"Writing model to file...")
            f.write(JSON)

        # Generate the item model
        JSON = """{
    "parent": "humility-afm:block/jack_o_lantern_""" + color + """"
}"""
        # Write the item model to the file
        with open(f"src/main/resources/assets/humility-afm/models/item/jack_o_lantern_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the blockstate
        JSON = """{
    "variants": {
        "facing=east": {
            "model": "humility-afm:block/jack_o_lantern_""" + color + """",
            "y": 90
        },
        "facing=north": {
            "model": "humility-afm:block/jack_o_lantern_""" + color + """"
        },
        "facing=south": {
            "model": "humility-afm:block/jack_o_lantern_""" + color + """",
            "y": 180
        },
        "facing=west": {
            "model": "humility-afm:block/jack_o_lantern_""" + color + """",
            "y": 270
        }
    }
}"""
        # Write the blockstate to the file
        with open(f"src/main/resources/assets/humility-afm/blockstates/jack_o_lantern_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the recipe
        JSON = """{
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
        {
            "item": "minecraft:""" + color + """_torch"
        },
        {
            "item": "minecraft:carved_pumpkin"
        }
    ],
    "result": {
        "item": "humility-afm:jack_o_lantern_""" + color + """"
    }
}"""
        # Write the recipe to the file
        with open(f"src/main/resources/data/humility-afm/recipes/jack_o_lantern_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the loot table
        JSON = """{
    "type": "minecraft:block",
    "pools": [
        {
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "humility-afm:jack_o_lantern_""" + color + """"
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
        # Write the loot table to the file
        with open(f"src/main/resources/data/humility-afm/loot_tables/blocks/jack_o_lantern_{color}.json", "w") as f:
            f.write(JSON)

if __name__ == "__main__":
    generateLEDVariantsJSONs()