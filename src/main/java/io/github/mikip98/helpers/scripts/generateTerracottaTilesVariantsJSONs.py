terracottaTypes = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

def generateWoodenMosaicVariantsJSONs():
    for terracottaType in terracottaTypes:
        for terracottaType2 in terracottaTypes:
            if terracottaType == terracottaType2:
                continue

            print("Generating terracotta tiles jsons for " + terracottaType + " and " + terracottaType2 + "...")

            # Generate model json
            JSON = """{
    "parent": "humility-afm:block/wooden_mosaic",
    "textures": {
        "1": "minecraft:block/""" + terracottaType + """_terracotta",
        "2": "minecraft:block/""" + terracottaType2 + """_terracotta"
    }
}"""

            # Write model json to file
            fileName = "terracotta_tiles_" + terracottaType + "_" + terracottaType2 + ".json"
            with open("src/main/resources/assets/humility-afm/models/block/" + fileName, "w") as file:
                file.write(JSON)
            # file = open("src/main/resources/assets/humility-afm/models/block/" + fileName, "w")
            # file.write(JSON)
            # file.close()

            # Generate item model json
            JSON = """{
    "parent": "humility-afm:block/""" + fileName[:-5] + """"
}"""

            # Write item model json to file
            with open("src/main/resources/assets/humility-afm/models/item/" + fileName, "w") as file:
                file.write(JSON)

            # Generate blockstate json
            JSON = """{
    "variants": {
        "": {
            "model": "humility-afm:block/""" + fileName[:-5] + """"
        }
    }
}"""

            # Write blockstate json to file
            with open("src/main/resources/assets/humility-afm/blockstates/" + fileName, "w") as file:
                file.write(JSON)

            # Generate recipes jsons
            JSON = """{
    "type": "minecraft:crafting_shaped",
    "pattern": [
        "AB",
        "BA"
    ],
    "key": {
        "A": {
            "item": "minecraft:""" + terracottaType + """_terracotta"
        },
        "B": {
            "item": "minecraft:""" + terracottaType2 + """_terracotta"
        }
    },
    "result": {
        "item": "humility-afm:""" + fileName[:-5] + """"
    }
}"""

            # Write recipe json to file
            with open("src/main/resources/data/humility-afm/recipes/" + fileName, "w") as file:
                file.write(JSON)

            # Generate AB -> BA recipe json
            JSON = """{
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
        {
            "item": "humility-afm:""" + fileName[:-5] + """"
        }
    ],
    "result": {
        "item": "humility-afm:terracotta_tiles_""" + terracottaType2 + "_" + terracottaType + """"
    }
}"""

            # Write recipe json to file
            # Remove .json from fileName
            fileName = fileName[:-5]
            fileName += "_A_to_B.json"
            with open("src/main/resources/data/humility-afm/recipes/" + fileName, "w") as file:
                file.write(JSON)


def generateWoodenMosaicVariantsNames():
    names = []
    for woodType in woodTypes:
        for woodType2 in woodTypes:
            if woodType == woodType2:
                continue
            names.append(woodType + "_" + woodType2)
    return names


generateWoodenMosaicVariantsJSONs()
