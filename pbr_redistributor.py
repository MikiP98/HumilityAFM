# coding=utf-8
import os.path
import shutil
from io import BytesIO

from PIL import Image


pbr_folder_prefix: str = "./PBR/"
resource_path_prefix: str = "./src/main/resources/resourcepacks/"


coloured_jack_o_lantern_file_list: list[str] = [
    "coloured_jack_o_lantern_black_s", "coloured_jack_o_lantern_blue_s",
    "coloured_jack_o_lantern_brown_s", "coloured_jack_o_lantern_cyan_s",
    "coloured_jack_o_lantern_gray_s", "coloured_jack_o_lantern_green_s",
    "coloured_jack_o_lantern_light_blue_s", "coloured_jack_o_lantern_light_gray_s",
    "coloured_jack_o_lantern_lime_s", "coloured_jack_o_lantern_magenta_s",
    "coloured_jack_o_lantern_orange_s", "coloured_jack_o_lantern_pink_s",
    "coloured_jack_o_lantern_purple_s", "coloured_jack_o_lantern_red_s",
    "coloured_jack_o_lantern_white_s", "coloured_jack_o_lantern_yellow_s"
]
coloured_torch_file_list: list[str] = [
    "coloured_torch_black_s", "coloured_torch_blue_s",
    "coloured_torch_brown_s", "coloured_torch_cyan_s",
    "coloured_torch_gray_s", "coloured_torch_green_s",
    "coloured_torch_light_blue_s", "coloured_torch_light_gray_s",
    "coloured_torch_lime_s", "coloured_torch_magenta_s",
    "coloured_torch_orange_s", "coloured_torch_pink_s",
    "coloured_torch_purple_s", "coloured_torch_red_s",
    "coloured_torch_white_s", "coloured_torch_yellow_s"
]


distribution_paths: dict[str, tuple[str, list[str]]] = {
    # Jack o'Lanterns
    "jack_o_lantern/face/coloured/": (
        "coloured_jack_o_lanterns_labpbr_emission_face/assets/humility-afm/textures/block/coloured_jack_o_lantern/",
        coloured_jack_o_lantern_file_list
    ),
    "jack_o_lantern/face/special/": (
        "special_jack_o_lanterns_labpbr_emission_face/assets/humility-afm/textures/block/",
        ["jack_o_lantern_redstone_s", "jack_o_lantern_soul_s"]
    ),
    "jack_o_lantern/smooth/coloured/": (
        "coloured_jack_o_lanterns_labpbr_emission_smooth/assets/humility-afm/textures/block/coloured_jack_o_lantern/",
        coloured_jack_o_lantern_file_list
    ),
    "jack_o_lantern/smooth/special/": (
        "special_jack_o_lanterns_labpbr_emission_smooth/assets/humility-afm/textures/block/",
        ["jack_o_lantern_redstone_s", "jack_o_lantern_soul_s"]
    ),
    # Coloured Torches
    "coloured_torch/sharp/": (
        "coloured_torch_labpbr_sharp/assets/humility-afm/textures/block/coloured_torch/",
        coloured_torch_file_list
    ),
    "coloured_torch/smooth/": (
        "coloured_torch_labpbr_smooth/assets/humility-afm/textures/block/coloured_torch/",
        coloured_torch_file_list
    ),
    # Cabinets
    "cabinet/": (
        "cabinet_labpbr_full/assets/humility-afm/textures/block/",
        ["cabinet_block_front_doors_s"]
    )
}


def main() -> None:
    print("Distributing '" + str(len(distribution_paths.keys())) + "' PBR textures")
    for key in distribution_paths.keys():
        input_path = pbr_folder_prefix + key
        print("Distributing:", input_path)

        input_img = combine_channels(input_path)

        # Save once into memory
        buffer = BytesIO()
        input_img.save(buffer, format="PNG", optimize=True, compress_level=9)
        buffer.seek(0)  # rewind to the beginning

        output_path, files = distribution_paths[key]
        for file in files:
            path = resource_path_prefix + output_path

            if not os.path.exists(path):
                os.makedirs(path)

            with open(path + file + ".png", "wb+") as f:
                shutil.copyfileobj(buffer, f)
            buffer.seek(0)  # rewind again for the next copy

    print("All done")


def combine_channels(path):
    # Open each image in grayscale mode (L = 8-bit pixels, black and white)
    if os.path.exists(path + "red.png"):
        red = Image.open(path + "red.png").convert("L")
    else:
        red = Image.open(path + "red.webp").convert("L")

    if os.path.exists(path + "green.png"):
        green = Image.open(path + "green.png").convert("L")
    else:
        green = Image.open(path + "green.webp").convert("L")

    if os.path.exists(path + "blue.png"):
        blue = Image.open(path + "blue.png").convert("L")
    else:
        blue = Image.open(path + "blue.webp").convert("L")

    if os.path.exists(path + "alpha.png"):
        alpha = Image.open(path + "alpha.png").convert("L")
    elif os.path.exists(path + "alpha.webp"):
        alpha = Image.open(path + "alpha.webp").convert("L")
    else:
        alpha = Image.new("L", (16, 16), color=255)

    # Merge into RGBA
    return Image.merge("RGBA", (red, green, blue, alpha))


if __name__ == "__main__":
    main()
