import utils


from mainPythonScriptsHelper import woodTypes


woodVariants = woodTypes
mudBricksVariant = "mud_bricks"
sandStoneANDQuartsVariants = ["quartz", "sandstone", "red_sandstone"]
stony1Variants = [
    "blackstone", "andesite", "polished_andesite", "diorite", "polished_diorite", "granite",
    "polished_granite", "polished_blackstone_brick", "prismarine", "dark_prismarine",
    "prismarine_bricks", "purpur", "stone", "stone_brick", "mossy_stone_brick"
]
stony2Variants = [
    "brick", "cobblestone", "mossy_cobblestone", "nether_brick", "red_nether_brick",
    "polished_blackstone", "smooth_quartz", "smooth_sandstone", "smooth_red_sandstone"
]
endStoneVariant = "end_stone_brick"
cutCopperVariants = ["cut_copper", "exposed_cut_copper", "weathered_cut_copper", "oxidized_cut_copper"]
deepSlateVariants = ["cobbled_deepslate", "polished_deepslate", "deepslate_brick", "deepslate_tile"]

AllVariants = (
        woodVariants +
        [mudBricksVariant] +
        sandStoneANDQuartsVariants +
        stony1Variants + stony2Variants +
        [endStoneVariant] +
        cutCopperVariants +
        deepSlateVariants
)


print("AllVariants: " + str(AllVariants))


def generateInnerOuterStairsVariantsJSONs():
    for variant in AllVariants:
        print(f"Generating inner and outer stairs variant JSONs for {variant}...")
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

        textures = {
            "bottom": f"minecraft:block/{bottom}{blockToUse}",
            "top": f"minecraft:block/{top}{blockToUse}",
            "side": f"minecraft:block/{side}{blockToUse}"
        }
        # Inner
        innerModelsJSON = {
            "parent": "humility-afm:block/stairs_inner",
            "textures": textures
        }
        utils.save_json(innerModelsJSON, f"assets/humility-afm/models/block/{innerName}.json")

        # Outer
        outerModelsJSON = {
            "parent": "humility-afm:block/stairs_outer",
            "textures": textures
        }
        utils.save_json(outerModelsJSON, f"assets/humility-afm/models/block/{outerName}.json")

        # Generate item models JSONs
        # Inner
        innerItemModelsJSON = {
            "parent": "humility-afm:block/" + innerName
        }
        utils.save_json(innerItemModelsJSON, f"assets/humility-afm/models/item/{innerName}.json")

        # Outer
        outerItemModelsJSON = {
            "parent": "humility-afm:block/" + outerName
        }
        utils.save_json(outerItemModelsJSON, f"assets/humility-afm/models/item/{outerName}.json")

        # Generate blockstates JSONs
        directions = [("north", 0), ("south", 180), ("west", 270), ("east", 90)]  # Top is +90
        # Inner
        model = f"humility-afm:block/{innerName}"
        variants = {}
        for direction, y in directions:
            variant_tj = {
                "model": model,
                "y": y,
                "uvlock": True
            }

            if variant_tj["y"] == 0:
                del variant_tj["y"]
                del variant_tj["uvlock"]

            variants[f"facing={direction},half=bottom"] = variant_tj

            variant_tj = {
                "model": model,
                "x": 180,
                "y": (y + 90) % 360,
                "uvlock": True
            }

            if variant_tj["y"] == 0:
                del variant_tj["y"]

            variants[f"facing={direction},half=top"] = variant_tj

        innerBlockstatesJSON = {
            "variants": variants
        }

        utils.save_json(innerBlockstatesJSON, f"assets/humility-afm/blockstates/{innerName}.json")

        # Outer
        model = f"humility-afm:block/{outerName}"
        variants = {}
        for direction, y in directions:
            variant_tj = {
                "model": model,
                "y": y,
                "uvlock": True
            }

            if variant_tj["y"] == 0:
                del variant_tj["y"]
                del variant_tj["uvlock"]

            variants[f"facing={direction},half=bottom"] = variant_tj

            variant_tj = {
                "model": model,
                "x": 180,
                "y": (y + 90) % 360,
                "uvlock": True
            }

            if variant_tj["y"] == 0:
                del variant_tj["y"]

            variants[f"facing={direction},half=top"] = variant_tj

        outerBlockstatesJSON = {
            "variants": variants
        }
        utils.save_json(outerBlockstatesJSON, f"assets/humility-afm/blockstates/{outerName}.json")

        # Create recipe JSONs
        if variant == "mud_bricks":
            variant = "mud_brick"
        elif variant == "prismarine_bricks":
            variant = "prismarine_brick"

        # Inner
        ingredient = {
            "item": f"minecraft:{variant}_stairs"
        }
        result = {
            "item": f"humility-afm:{innerName}"
        }
        innerRecipeJSON = utils.generate_shapeless_recipe(result, [ingredient])
        utils.save_json(innerRecipeJSON, f"data/humility-afm/recipes/{innerName}.json")

        # Outer
        ingredient = {
            "item": f"humility-afm:{innerName}"
        }
        result = {
            "item": f"humility-afm:{outerName}"
        }
        outerRecipeJSON = utils.generate_shapeless_recipe(result, [ingredient])
        utils.save_json(outerRecipeJSON, f"data/humility-afm/recipes/{outerName}.json")

        # Back to normal
        ingredient = {
            "item": f"humility-afm:{outerName}"
        }
        result = {
            "item": f"minecraft:{variant}_stairs"
        }
        stairsRecipeJSON = utils.generate_shapeless_recipe(result, [ingredient])
        utils.save_json(stairsRecipeJSON, f"data/humility-afm/recipes/{outerName}_to_vanilla.json")

        # Generate the loot table JSONs
        # Inner
        json = utils.generate_simple_loot_table(f"humility-afm:{innerName}")
        utils.save_json(json, f"data/humility-afm/loot_tables/blocks/{innerName}.json")
        # Outer
        json = utils.generate_simple_loot_table(f"humility-afm:{outerName}")
        utils.save_json(json, f"data/humility-afm/loot_tables/blocks/{outerName}.json")

    print("Generated inner and outer stairs variants JSONs!")


def generateInnerOuterStairsVariantsWoodNames():
    return woodVariants


def generateInnerOuterStairsVariantsStoneNames():
    return (
            [mudBricksVariant] +
            sandStoneANDQuartsVariants +
            stony1Variants +
            stony2Variants +
            [endStoneVariant] +
            cutCopperVariants +
            deepSlateVariants
    )


def generateInnerOuterStairsVariantsAllNames():
    return AllVariants


if __name__ == "__main__":
    print("Generating inner and outer stairs variants JSONs...")
    generateInnerOuterStairsVariantsJSONs()
