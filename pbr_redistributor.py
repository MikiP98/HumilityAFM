# coding=utf-8
import os.path
import shutil
from io import BytesIO

from PIL import Image

resource_path_prefix: str = "./src/main/resources/resourcepacks/"

distribution_paths: dict[str, tuple[str, list[str]]] = {
    "./jack_o_lantern_pbr/coloured/": (
        "coloured_jack_o_lanterns_labpbr_emission_face/assets/humility-afm/textures/block/coloured_jack_o_lantern/",
        [
            "coloured_jack_o_lantern_black_s", "coloured_jack_o_lantern_blue_s",
            "coloured_jack_o_lantern_brown_s", "coloured_jack_o_lantern_cyan_s",
            "coloured_jack_o_lantern_gray_s", "coloured_jack_o_lantern_green_s",
            "coloured_jack_o_lantern_light_blue_s", "coloured_jack_o_lantern_light_gray_s",
            "coloured_jack_o_lantern_lime_s", "coloured_jack_o_lantern_magenta_s",
            "coloured_jack_o_lantern_orange_s", "coloured_jack_o_lantern_pink_s",
            "coloured_jack_o_lantern_purple_s", "coloured_jack_o_lantern_red_s",
            "coloured_jack_o_lantern_white_s", "coloured_jack_o_lantern_yellow_s"
        ]
    ),
    "./jack_o_lantern_pbr/special/": (
        "special_jack_o_lanterns_labpbr_emission_face/assets/humility-afm/textures/block/",
        ["jack_o_lantern_redstone_s", "jack_o_lantern_soul_s"]
    )
}


def main() -> None:
    print("Distributing '" + str(len(distribution_paths.keys())) + "' PBR textures")
    for key in distribution_paths.keys():
        print("Distributing:", key)

        grayscaleify(key)
        input_img = combine_channels(key)

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
    red = Image.open(path + "red.png").convert("L")
    green = Image.open(path + "green.png").convert("L")
    blue = Image.open(path + "blue.png").convert("L")
    alpha = Image.open(path + "alpha.png").convert("L")

    # Merge into RGBA
    return Image.merge("RGBA", (red, green, blue, alpha))


def grayscaleify(path):
    red = Image.open(path + "red.png").convert("L")
    red.save(path + "red.png", format="PNG", optimize=True, compress_level=9)
    green = Image.open(path + "green.png").convert("L")
    green.save(path + "green.png", format="PNG", optimize=True, compress_level=9)
    blue = Image.open(path + "blue.png").convert("L")
    blue.save(path + "blue.png", format="PNG", optimize=True, compress_level=9)
    alpha = Image.open(path + "alpha.png").convert("L")
    alpha.save(path + "alpha.png", format="PNG", optimize=True, compress_level=9)


if __name__ == "__main__":
    main()
