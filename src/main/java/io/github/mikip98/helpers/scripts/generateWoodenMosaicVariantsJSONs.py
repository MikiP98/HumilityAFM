from mainPythonScriptsHelper import woodTypes


def generateWoodenMosaicVariantsJSONs():
    for woodType in woodTypes:
        for woodType2 in woodTypes:
            if woodType == woodType2:
                continue

            print("Generating wooden mosaic jsons for " + woodType + " and " + woodType2 + "...")

            # Generate model json
            JSON = """{
    "parent": "humility-afm:block/wooden_mosaic",
    "textures": {
        "1": "minecraft:block/""" + woodType + """_planks",
        "2": "minecraft:block/""" + woodType2 + """_planks"
    }
}"""

            # Write model json to file
            fileName = "wooden_mosaic_" + woodType + "_" + woodType2 + ".json"
            with open("src/main/resources/assets/humility-afm/models/block/" + fileName, "w") as file:
                file.write(JSON)
            # file = open("src/main/resources/assets/humility-afm/models/block/" + fileName, "w")
            # file.write(JSON)
            # file.close()

            # Generate item model json
            JSON = """{
    "parent": "humility-afm:block/wooden_mosaic_""" + woodType + "_" + woodType2 + """"
}"""

            # Write item model json to file
            with open("src/main/resources/assets/humility-afm/models/item/" + fileName, "w") as file:
                file.write(JSON)

            # Generate blockstate json
            JSON = """{
    "variants": {
        "": {
            "model": "humility-afm:block/wooden_mosaic_""" + woodType + "_" + woodType2 + """"
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
            "item": "minecraft:""" + woodType + """_planks"
        },
        "B": {
            "item": "minecraft:""" + woodType2 + """_planks"
        }
    },
    "result": {
        "item": "humility-afm:wooden_mosaic_""" + woodType + "_" + woodType2 + """"
    }
}"""

            # Write recipe json to file
            fileName = "wooden_mosaic_" + woodType + "_" + woodType2 + ".json"
            with open("src/main/resources/data/humility-afm/recipes/" + fileName, "w") as file:
                file.write(JSON)

            # Generate AB -> BA recipe json
            JSON = """{
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
        {
            "item": "humility-afm:wooden_mosaic_""" + woodType + "_" + woodType2 + """"
        }
    ],
    "result": {
        "item": "humility-afm:wooden_mosaic_""" + woodType2 + "_" + woodType + """"
    }
}"""

            # Write recipe json to file
            fileName = "wooden_mosaic_" + woodType2 + "_" + woodType + "_A_to_B.json"
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
