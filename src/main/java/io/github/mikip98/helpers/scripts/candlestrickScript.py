import utils


metalTypes = {
    "gold", "copper", "exposed_copper", "weathered_copper", "oxidized_copper",
    "waxed_copper", "waxed_exposed_copper", "waxed_weathered_copper", "waxed_oxidized_copper"
}


def generateNames() -> list[str]:
    from generateCabinetBlockVariantsJSONs import vanilla_wool_types
    cabinetBlockVariantsNames = []
    for metalType in metalTypes:
        cabinetBlockVariantsNames.append("candlestick_" + metalType)
        cabinetBlockVariantsNames.append("candlestick_" + metalType + "_candle")
        for woolType in vanilla_wool_types:
            cabinetBlockVariantsNames.append("candlestick_" + metalType + "_candle_" + woolType)
    print(cabinetBlockVariantsNames)
    return cabinetBlockVariantsNames


def generateJSONs():
    # Generate candlestick blockstates
    placableNames: list[str] = generateNames()
    for name in placableNames:
        words = frozenset(name.split("_"))
        if "candle" in words:
            json = {
                "variants": {
                    "facing=north,lit=false": {"model": f"humility-afm:block/{name}", "y": 180},
                    "facing=east,lit=false": {"model": f"humility-afm:block/{name}", "y": 270},
                    "facing=south,lit=false": {"model": f"humility-afm:block/{name}"},
                    "facing=west,lit=false": {"model": f"humility-afm:block/{name}", "y": 90},

                    "facing=north,lit=true": {"model": f"humility-afm:block/{name}_lit", "y": 180},
                    "facing=east,lit=true": {"model": f"humility-afm:block/{name}_lit", "y": 270},
                    "facing=south,lit=true": {"model": f"humility-afm:block/{name}_lit"},
                    "facing=west,lit=true": {"model": f"humility-afm:block/{name}_lit", "y": 90}
                }
            }
        else:
            json = {
                "variants": {
                    "facing=north": {"model": f"humility-afm:block/{name}", "y": 180},
                    "facing=east": {"model": f"humility-afm:block/{name}", "y": 270},
                    "facing=south": {"model": f"humility-afm:block/{name}"},
                    "facing=west": {"model": f"humility-afm:block/{name}", "y": 90}
                }
            }

        # directory = "src/main/resources/assets/humility-afm/blockstates/"
        # os.makedirs(directory, exist_ok=True)
        print(f"src/main/resources/assets/humility-afm/blockstates/{name}.json")
        utils.save_json(json, f"assets/humility-afm/blockstates/{name}.json")

    # Generate candlestick models
    from generateCabinetBlockVariantsJSONs import vanilla_wool_types
    for metalType in metalTypes:
        is_block = ""
        if metalType == "gold" or metalType == "copper" or metalType == "waxed_copper":
            is_block = "_block"
        metal_texture = metalType.replace("waxed_", '')

        JSON = {
            "parent": "humility-afm:block/candlestick",
            "textures": {
                "0": f"block/{metal_texture}{is_block}",
                "particle": f"block/{metal_texture}{is_block}"
            }
        }
        utils.save_json(JSON, f"assets/humility-afm/models/block/candlestick_{metalType}.json")

        JSON = {
            "parent": "humility-afm:block/candlestick_candle",
            "textures": {
                "0": f"block/{metal_texture}{is_block}",
                "2": "block/candle",
                "particle": f"block/{metal_texture}{is_block}"
            }
        }
        utils.save_json(JSON, f"assets/humility-afm/models/block/candlestick_{metalType}_candle.json")

        JSON = {
            "parent": "humility-afm:block/candlestick_candle",
            "textures": {
                "0": f"block/{metal_texture}{is_block}",
                "2": "block/candle_lit",
                "particle": f"block/{metal_texture}{is_block}"
            }
        }
        utils.save_json(JSON, f"assets/humility-afm/models/block/candlestick_{metalType}_candle_lit.json")

        for woolType in vanilla_wool_types:
            JSON = {
                "parent": "humility-afm:block/candlestick_candle",
                "textures": {
                    "0": f"block/{metal_texture}{is_block}",
                    "2": f"block/{woolType}_candle",
                    "particle": f"block/{metal_texture}{is_block}"
                }
            }
            utils.save_json(
                JSON,
                f"assets/humility-afm/models/block/candlestick_{metalType}_candle_{woolType}.json"
            )

        for woolType in vanilla_wool_types:
            JSON = {
                "parent": "humility-afm:block/candlestick_candle",
                "textures": {
                    "0": f"block/{metal_texture}{is_block}",
                    "2": f"block/{woolType}_candle_lit",
                    "particle": f"block/{metal_texture}{is_block}"
                }
            }
            utils.save_json(JSON, f"assets/humility-afm/models/block/candlestick_{metalType}_{woolType}_lit.json")

    # Generate candlestick item models
    for metalType in metalTypes:
        JSON = {
            "parent": f"humility-afm:block/candlestick_{metalType}"
        }
        utils.save_json(JSON, f"assets/humility-afm/models/item/candlestick_{metalType}.json")

        JSON = {
            "parent": f"humility-afm:block/candlestick_{metalType}_candle"
        }
        utils.save_json(JSON, f"assets/humility-afm/models/item/candlestick_{metalType}_candle.json")

        for woolType in vanilla_wool_types:
            JSON = {
                "parent": f"humility-afm:block/candlestick_{metalType}_candle_{woolType}"
            }
            utils.save_json(JSON, f"assets/humility-afm/models/item/candlestick_{metalType}_candle_{woolType}.json")

    # Generate candlestick loot tables
    for name in placableNames:
        # print()
        # print(name)
        words = name.split("_")

        if "candle" not in words:
            JSON = utils.generate_simple_loot_table(f"humility-afm:{name}")
        else:
            # split the name into empty candlestick and candle
            i = 0
            candlestick = ""
            while words[i] != "candle" and words[i] not in vanilla_wool_types:
                candlestick += words[i] + "_"
                i += 1
                if i >= len(words):
                    break
            candlestick = candlestick[:-1]
            candle = '_'.join(words[i+1:])

            items = [
                f"humility-afm:{candlestick}",
                f"minecraft:{candle}_candle"
            ]
            if items[1] == "minecraft:_candle":
                items[1] = f"minecraft:candle"

            JSON = utils.generate_multi_loot_table(items)

        utils.save_json(JSON, f"data/humility-afm/loot_tables/blocks/{name}.json")

    # Generate candlestick recipes
    pattern = [
        "M ",
        "MM"
    ]
    for metal_type in ("gold", "copper"):
        key = {
            "M": {
                "item": f"minecraft/{metal_type}_ingot"
            }
        }
        result = {
            "item": f"humility-afm:candlestick_{metal_type}"
        }
        json = utils.generate_shaped_recipe(result, pattern, key)
        utils.save_json(json, f"data/humility-afm/recipes/candlestick_{metal_type}.json")

    pattern = [
        " M",
        "MM"
    ]
    for metal_type in ("gold", "copper"):
        key = {
            "M": {
                "item": f"minecraft:{metal_type}_ingot"
            }
        }
        result = {
            "item": f"humility-afm:candlestick_{metal_type}"
        }
        json = utils.generate_shaped_recipe(result, pattern, key)
        utils.save_json(json, f"data/humility-afm/recipes/candlestick_{metal_type}_mirror.json")


if __name__ == "__main__":
    generateJSONs()
