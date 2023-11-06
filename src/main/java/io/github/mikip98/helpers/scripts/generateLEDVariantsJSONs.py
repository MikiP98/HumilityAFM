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
        "": { "model": "humility-afm:led_""" + color + """" }
    }
}"""
        # Write the blockstate to the file
        with open(f"src/main/resources/assets/humility-afm/blockstates/led_{color}.json", "w") as f:
            f.write(JSON)


if __name__ == "__main__":
    generateLEDVariantsJSONs()
