import json

from typing import NotRequired, TypedDict


class Item(TypedDict):
    item: str
    data: NotRequired[int]
    count: NotRequired[int]


class ShapelessRecipe(TypedDict):
    type: str
    ingredients: list[Item]
    result: Item


def generate_shapeless_recipe(output: Item, ingredients: list[Item]) -> ShapelessRecipe:
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": ingredients,
        "result": output
    }


class ShapedRecipe(TypedDict):
    type: str
    pattern: list[str]
    key: dict[str, Item]
    result: Item


def generate_shaped_recipe(output: Item, pattern: list[str], key: dict[str, Item]) -> ShapedRecipe:
    unique_chars = set("".join(pattern))
    # Check if all keys are used
    for char in unique_chars:
        if char not in key and char != " ":
            raise ValueError(f"Key '{char}' is not specified in the key!")

    return {
        "type": "minecraft:crafting_shaped",
        "pattern": pattern,
        "key": key,
        "result": output
    }


class Condition(TypedDict):
    condition: str


class Entry(TypedDict):
    type: str
    name: str


class Pool(TypedDict):
    rolls: int
    entries: list[dict]
    conditions: list[dict]


class LootTable(TypedDict):
    type: str
    pools: list[Pool]


def generate_loot_table(pools: list[Pool]) -> LootTable:
    return {
        "type": "minecraft:block",
        "pools": pools
    }


def generate_simple_loot_table(item: str) -> LootTable:
    return generate_multi_loot_table([item])


def generate_multi_loot_table(items: list[str]) -> LootTable:
    pools = []
    for item in items:
        pools.append({
            "rolls": 1,
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": item
                }
            ],
            "conditions": [
                {
                    "condition": "minecraft:survives_explosion"
                }
            ]
        })

    return {
        "type": "minecraft:block",
        "pools": pools
    }


def save_json(
        data: dict | ShapelessRecipe | ShapedRecipe,
        path: str
):
    with open("src/main/resources/" + path, "w+") as file:
        json.dump(data, file, indent=4)
