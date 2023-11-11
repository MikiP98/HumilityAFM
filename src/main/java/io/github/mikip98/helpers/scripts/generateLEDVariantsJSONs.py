colors = ["red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown", "black", "gray", "light_gray", "white"]

def generateLEDVariantsJSONs():
    print("Generating LED variants JSONs...")
    for color in colors:
        print(f"Generating LED variants JSON for {color}...")
        # Generate the model
        JSON = """{
    "parent": "humility-afm:block/led",
    "textures": {
        "0": "block/""" + color + """_terracotta",
        "particle": "block/""" + color + """_terracotta"
    }
}"""
        # Write the model to the file
        with open(f"src/main/resources/assets/humility-afm/models/block/led_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the inner model
        JSON = """{
    "parent": "humility-afm:block/led_inner",
    "textures": {
        "0": "block/""" + color + """_terracotta",
        "particle": "block/""" + color + """_terracotta"
    }
}"""
        # Write the inner model to the file
        with open(f"src/main/resources/assets/humility-afm/models/block/led_inner_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the outer model
        JSON = """{
    "parent": "humility-afm:block/led_outer",
    "textures": {
        "0": "block/""" + color + """_terracotta",
        "particle": "block/""" + color + """_terracotta"
    }
}"""
        # Write the outer model to the file
        with open(f"src/main/resources/assets/humility-afm/models/block/led_outer_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the item model
        JSON = """{
    "parent": "humility-afm:block/led_""" + color + """"
}"""
        # Write the item model to the file
        with open(f"src/main/resources/assets/humility-afm/models/item/led_{color}.json", "w") as f:
            f.write(JSON)

        # Generate the blockstate
        JSON = """{
    "variants": {
        "facing=east,half=bottom,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "uvlock": true
        },
        "facing=east,half=bottom,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=east,half=bottom,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "uvlock": true
        },
        "facing=east,half=bottom,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=east,half=bottom,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=east,half=top,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 270,
            "uvlock": true
        },
        "facing=east,half=top,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """"
        },
        "facing=east,half=top,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 270,
            "uvlock": true
        },
        "facing=east,half=top,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """"
        },
        "facing=east,half=top,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "y": 270,
            "uvlock": true
        },
        "facing=north,half=bottom,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=north,half=bottom,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "uvlock": true
        },
        "facing=north,half=bottom,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=north,half=bottom,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "uvlock": true
        },
        "facing=north,half=bottom,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "x": 180,
            "uvlock": true
        },
        "facing=north,half=top,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 180,
            "uvlock": true
        },
        "facing=north,half=top,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 270,
            "uvlock": true
        },
        "facing=north,half=top,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 180,
            "uvlock": true
        },
        "facing=north,half=top,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 270,
            "uvlock": true
        },
        "facing=north,half=top,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=bottom,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=bottom,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=bottom,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=bottom,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=bottom,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=top,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """"
        },
        "facing=south,half=top,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=top,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """"
        },
        "facing=south,half=top,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=top,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """"
        },
        "facing=west,half=bottom,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=west,half=bottom,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=bottom,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=west,half=bottom,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=bottom,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=top,shape=inner_left": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 90,
            "uvlock": true
        },
        "facing=west,half=top,shape=inner_right": {
            "model": "humility-afm:block/led_inner_""" + color + """",
            "y": 180,
            "uvlock": true
        },
        "facing=west,half=top,shape=outer_left": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 90,
            "uvlock": true
        },
        "facing=west,half=top,shape=outer_right": {
            "model": "humility-afm:block/led_outer_""" + color + """",
            "y": 180,
            "uvlock": true
        },
        "facing=west,half=top,shape=straight": {
            "model": "humility-afm:block/led_""" + color + """",
            "y": 90,
            "uvlock": true
        }
    }
}"""
        # Write the blockstate to the file
        with open(f"src/main/resources/assets/humility-afm/blockstates/led_{color}.json", "w") as f:
            f.write(JSON)


if __name__ == "__main__":
    generateLEDVariantsJSONs()
