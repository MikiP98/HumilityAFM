from mainPythonScriptsHelper import woodTypes


woodVariants = woodTypes
mudBricksVariant = "mud_bricks"
sandStoneANDQuartsVariants = ["quartz", "sandstone", "red_sandstone"]
stony1Variants = ["blackstone", "andesite", "polished_andesite", "diorite", "polished_diorite", "granite", "polished_granite", "polished_blackstone_brick", "prismarine", "dark_prismarine", "prismarine_bricks", "purpur", "stone", "stone_brick", "mossy_stone_brick"]
stony2Variants = ["brick", "cobblestone", "mossy_cobblestone", "nether_brick", "red_nether_brick", "polished_blackstone", "smooth_quartz", "smooth_sandstone", "smooth_red_sandstone"]
endStoneVariant = "end_stone_brick"
cutCopperVariants = ["cut_copper", "exposed_cut_copper", "weathered_cut_copper", "oxidized_cut_copper"]
deepSlateVariants = ["cobbled_deepslate", "polished_deepslate", "deepslate_brick", "deepslate_tile"]

AllVariants = woodVariants + [mudBricksVariant] + sandStoneANDQuartsVariants + stony1Variants + stony2Variants + [endStoneVariant] + cutCopperVariants + deepSlateVariants


print("AllVariants: " + str(AllVariants))


def generateCabinetBlockVariantsJSONs(override = False):
    for variant in AllVariants:
        print("Generating cabinet block variants JSON for " + variant + "...")
        innerName = "inner_stairs_" + variant
        outerName = "outer_stairs_" + variant

        # Generate models JSONs
        # What block should be used for the model
        top = variant
        side = variant
        bottom = variant
        def cutBrick(name):
            return name.replace("_brick", "")

        blockToUse = ""

        if variant in woodVariants:  # Correct
            blockToUse = "_planks"
        elif variant == mudBricksVariant:  # Correct
            pass
        elif variant in sandStoneANDQuartsVariants:  # Correct
            if variant == "quartz":  # Works!
                side = side + "_block_side"
                top = side
                bottom = side
            elif variant == "sandstone":  # Works!
                top = top + "_top"
                bottom = bottom + "_bottom"
            elif variant == "red_sandstone":  # Works!
                top = top + "_top"
                bottom = bottom + "_bottom"
        elif variant in stony1Variants:  # Correct
            if variant == "polished_blackstone_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
            elif variant == "purpur":  # Works!
                blockToUse = "_block"
            elif variant == "stone_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
            elif variant == "mossy_stone_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
        elif variant in stony2Variants:  # Correct
            if variant == "brick":  # Works!
                top = ""
                side = ""
                bottom = ""
                blockToUse = "bricks"
            elif variant == "nether_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
            elif variant == "red_nether_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
            elif variant == "smooth_quartz":  # Works!
                bottom = "quartz_block_bottom"
                top = bottom
                side = bottom
            elif variant == "smooth_sandstone":  # Works!
                top = "sandstone_top"
                side = top
                bottom = top
            elif variant == "smooth_red_sandstone":  # Works!
                top = "red_sandstone_top"
                side = top
                bottom = top
        elif variant == endStoneVariant:  # Correct
            top = cutBrick(top)
            side = cutBrick(side)
            bottom = cutBrick(bottom)
            blockToUse = "_bricks"
        elif variant in cutCopperVariants:  # Correct
            pass
        elif variant in deepSlateVariants:  # Correct
            if variant == "deepslate_brick":  # Works!
                top = cutBrick(top)
                side = cutBrick(side)
                bottom = cutBrick(bottom)
                blockToUse = "_bricks"
            elif variant == "deepslate_tile":  # Works!
                top = top.replace("tile", "")
                side = side.replace("tile", "")
                bottom = bottom.replace("tile", "")
                blockToUse = "tiles"

        # Inner
        innerModelsJSON = """{
    "parent": "humility-afm:block/stairs_inner",
    "textures": {
        "bottom": "minecraft:block/""" + bottom + blockToUse + """",
        "top": "minecraft:block/""" + top + blockToUse + """",
        "side": "minecraft:block/""" + side + blockToUse + """"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/" + innerName + ".json", "w") as f:
            f.write(innerModelsJSON)

        # Outer
        outerModelsJSON = """{
    "parent": "humility-afm:block/stairs_outer",
    "textures": {
        "bottom": "minecraft:block/""" + bottom + blockToUse + """",
        "top": "minecraft:block/""" + top + blockToUse + """",
        "side": "minecraft:block/""" + side + blockToUse + """"
    }
}"""
        with open("src/main/resources/assets/humility-afm/models/block/" + outerName + ".json", "w") as f:
            f.write(outerModelsJSON)

        # Generate item models JSONs
        # Inner
        innerItemModelsJSON = """{
    "parent": "humility-afm:block/""" + innerName + """"
}"""
        with open("src/main/resources/assets/humility-afm/models/item/" + innerName + ".json", "w") as f:
            f.write(innerItemModelsJSON)

        # Outer
        outerItemModelsJSON = """{
    "parent": "humility-afm:block/""" + outerName + """"
}"""
        with open("src/main/resources/assets/humility-afm/models/item/" + outerName + ".json", "w") as f:
            f.write(outerItemModelsJSON)

        # Generate blockstates JSONs
        # Inner
        innerBlockstatesJSON = """{
    "variants": {
        "facing=east,half=bottom": {
            "model": "humility-afm:block/""" + innerName + """",
            "y": 90,
            "uvlock": true
        },
        "facing=east,half=top": {
            "model": "humility-afm:block/""" + innerName + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=north,half=bottom": {
            "model": "humility-afm:block/""" + innerName + """"
        },
        "facing=north,half=top": {
            "model": "humility-afm:block/""" + innerName + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=bottom": {
            "model": "humility-afm:block/""" + innerName + """",
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=top": {
            "model": "humility-afm:block/""" + innerName + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=bottom": {
            "model": "humility-afm:block/""" + innerName + """",
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=top": {
            "model": "humility-afm:block/""" + innerName + """",
            "x": 180,
            "uvlock": true
        }
    }
}"""
        with open("src/main/resources/assets/humility-afm/blockstates/" + innerName + ".json", "w") as f:
            f.write(innerBlockstatesJSON)

        # Outer
        outerBlockstatesJSON = """{
    "variants": {
        "facing=east,half=bottom": {
            "model": "humility-afm:block/""" + outerName + """",
            "y": 90,
            "uvlock": true
        },
        "facing=east,half=top": {
            "model": "humility-afm:block/""" + outerName + """",
            "x": 180,
            "y": 180,
            "uvlock": true
        },
        "facing=north,half=bottom": {
            "model": "humility-afm:block/""" + outerName + """"
        },
        "facing=north,half=top": {
            "model": "humility-afm:block/""" + outerName + """",
            "x": 180,
            "y": 90,
            "uvlock": true
        },
        "facing=south,half=bottom": {
            "model": "humility-afm:block/""" + outerName + """",
            "y": 180,
            "uvlock": true
        },
        "facing=south,half=top": {
            "model": "humility-afm:block/""" + outerName + """",
            "x": 180,
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=bottom": {
            "model": "humility-afm:block/""" + outerName + """",
            "y": 270,
            "uvlock": true
        },
        "facing=west,half=top": {
            "model": "humility-afm:block/""" + outerName + """",
            "x": 180,
            "uvlock": true
        }
    }
}"""
        with open("src/main/resources/assets/humility-afm/blockstates/" + outerName + ".json", "w") as f:
            f.write(outerBlockstatesJSON)


        # Create recipe JSONs
        if variant == "mud_bricks":
            variant = "mud_brick"
        elif variant == "prismarine_bricks":
            variant = "prismarine_brick"
        # Inner
        innerRecipeJSON = """{
    "type": "crafting_shapeless",
    "ingredients": [
        {
            "item": "minecraft:""" + variant + """_stairs"
        }
    ],
    "result": {
        "item": "humility-afm:""" + innerName + """"
    }
}"""
        with open("src/main/resources/data/humility-afm/recipes/" + innerName + ".json", "w") as f:
            f.write(innerRecipeJSON)

        # Outer
        outerRecipeJSON = """{
    "type": "crafting_shapeless",
    "ingredients": [
        {
            "item": "humility-afm:""" + innerName + """"
        }
    ],
    "result": {
        "item": "humility-afm:""" + outerName + """"
    }
}"""
        with open("src/main/resources/data/humility-afm/recipes/" + outerName + ".json", "w") as f:
            f.write(outerRecipeJSON)

        # Back to normal
        stairsRecipeJSON = """{
    "type": "crafting_shapeless",
    "ingredients": [
        {
            "item": "humility-afm:""" + outerName + """"
        }
    ],
    "result": {
        "item": "minecraft:""" + variant + """_stairs"
    }
}"""
        with open("src/main/resources/data/humility-afm/recipes/" + outerName + "_to_vanilla.json", "w") as f:
            f.write(stairsRecipeJSON)

    print("Generated cabinet block variants JSONs!")


print("Generating cabinet block variants JSONs...")
generateCabinetBlockVariantsJSONs()