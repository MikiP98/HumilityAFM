# HumilityAFM
### Humility - Another Furniture Mod


## Description
This is a simple and small mod that adds some simple decorative blocks such as cabinets (which are fancier types of item frames) as well as some vanilla block variations.
<br>
It specializes in procedurally generating variants of the same block so that you can always find the look you were searching for.

### Download
The only official downloads are from [**Modrinth**](https://modrinth.com/mod/humility-afm/versions) and [Github](https://github.com/MikiP98/HumilityAFM)  
I can't ensure any other downloads are safe and don't include viruses  
PLS download the mod from Modrinth if you can


## Features:

### Almost fully resourcepack compatible!

HumilityAFM uses mostly the existing vanilla textures,  
So most resourcepacks should work with it out of the box!  
There are some included resourcepacks that improve the compatibility even more :)

[//]: # (TODO: Add image with resourcepacks active)

### Cabinets

Make your treasures pop!  
Match the wood to your build and wool interior to your item!  
<img width="75%" alt="Cabinet Block Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/6c6be08f82249beb49aad2ea2bf9b6f41cbf5e82.png">
<br>
Need something brighter? Illuminated Cabinet comes for rescue!  
<br>

### Wooden Mosaics!

Mix 2 different wood types to find your look!  
<img width="75%" alt="Wooden Mosaic Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/aa4b885e5d8f0efa85813671d841fad77358e0fb.png">
<br>
<br>

### Terracotta Tiles!

*It’s like wooden mosaic but flatter.*  
<img width="75%" alt="Terracotta Tiles Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/f4d165ecabe01bfba994ecbe28b813b1971d9e68.png">
<br>
<br>

### Forced Corner Stairs variants

Make the vanillest armchairs, sofas, tables and more!  
<img width="75%" alt="Forced Corner Stairs Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/819ebd4a9abf9eacd4c85205768f6d454e538b7e.png">
<br>
<br>

### Light Strips <sup>*(need to be enabled in the config)*</sup>

Lighten up your builds with unobtrusive light source  
<img width="75%" alt="LED Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/e489ba0d65132aa05c279cf5ef3f3577a41f4bc7.png">
<br>
**LEDs** also support **Bliss** shader and **Shimmer**'s mod coloured lights!  
<img width="75%" alt="LED coloured light Image" src="https://cdn.modrinth.com/data/RoBSx69i/images/7ff0a81c3e24eb38e83edf01f2c21e24a6b911e9.png">
<br>
<br>

### Soul and Redstone Jack o'Lanterns! <sup>*(With Shimmer support!)*</sup>

<img width="75%" alt="Soul and Redstone Pumpkins" src="https://cdn.modrinth.com/data/RoBSx69i/images/b0c38755d1e578ac181a0e7fa7e862bafa89781c.png">
<br>
<br>

### Candlesticks! <sup>*(need to be enabled in the config)*</sup>

<img width="75%" alt="Candlesticks in a tunnel" src="https://cdn.modrinth.com/data/RoBSx69i/images/75aca4cacab007202b4d56215beaafb3a17971f6.webp">
<br>
<br>

### High config customizability

[//]: # (TODO: List all the config options)

<br>


## Roadmap:

### High priority:

- Redo the window capture screenshots or at least crop the window app bar
- Fix Cabinet breaking animation being invisible:
  - Make sure all other breaking animations are also visible
- Finish built-in jack o'Lantern rp compat resourcepack
- Finish built-in coloured torches rp compat resourcepack
- Add 'Shimmer' and 'Bliss' support for all the blocks from coloured feature set:
  - Coloured Torches
  - Coloured Jack o'Lanterns
  - Light Strips
- Re-add Better Nether built-in support
- Add Better End built-in support
- Add Biomes o' Plenty built-in support
- Fix nether wood block variants burning
- Add Illuminated Cabinet Brightening to the config
- Add PBR data to:
  - Cabinet front texture:
    - LabPBR specular (pixels only)
    - Optional resourcepacks for:
      - LabPBR specular (full texture)
  - Jack o'Lanterns textures:
    - vanilla emission (coloured face only)
    - LabPBR emission (coloured face only)
    - Optional resourcepacks for:
      - vanilla emission (full texture)
      - LabPBR emission (full texture)
- Redo coloured torch textures using Jack o'Lantern palette system

### Medium priority:

- Improve the speciality of Soul Jack o'Lantern :/
- Add Fancy carpets
- Add hanging pots
- Add hanging candlesticks
- Add more candlestick variants:
  - small, medium, large, etc.
- Add correct map colours to generated blocks:
  - Cabinets and Illuminated Cabinets
  - Candlesticks
  - Wooden Mosaics
  - Terracotta Tiles
  - Light Strips
  - Coloured Torches
  - Jack o'Lanterns
  - Forced Corner Stairs
- Rethink cabinet opening, item insertion and removal, and such
- Add the ability to rotate items displayed in the cabinets
- Port to newer MC versions:
  - 1.21.4 *(need to wait for 'polymorph')*
- Expand Light Stips:
  - Vertical mount
  - Fit multiple Light Strips in 1 block
- Make coloured flame particles for coloured torches

### Low/unknown priority:

- Add leather variants of the cabinets?
- Make the leather cabinets dyable?
- Add carpet covered stairs?
- Server feature sync?
  - If done through a datapack, the blocks can be enabled by default on the client?
- Port to newer MC versions:
  - 1.21.5? *(need to wait for 'polymorph')*
- Backport to older MC versions?:
  - 1.19.2?
  - 1.18.2?
  - 1.16.5?
  - 1.12.2?
  - 1.8.9?
  - 1.7.10?
  - 1.0?
- Add crafting table on a stick?:
  - If the right-clicked block has a recipie that requires only itself and output only 1 other block, it will replace the right-clicked block with the output block from that recipe
- Add variant choosing block?:
  - A GUI block that will show all the variants that can be crafted in the crafting table following the *crafting table on a stick* logic, and it will allow you to choose the variant you want to craft
  - Should be really useful for candlesticks if they would get a lot of variants
- Make Light Strips and Candlesticks into integrated datapacks?:
  - Because of new tag system, the vanilla tool tags should no longer break, so at most this will fix the console spam with disabled features 
- Make BetterNether and BetterEnd support into integrated datapacks?
  - Because of new tag system, the vanilla tool tags should no longer break, so at most this will fix the console spam with disabled features
- Consider doing a HumilityLib library mod for config or such?
- Improve Illuminated Cabinet Brightening rendering?
- Add thin <sup>*(Blibiocraft style)*</sup> and short cabinets?
- Add a feature to connect 2 neighboring Cabinets into a big one?

<br>


## FAQ:

**Q:** Can you update/port the mod to MC x.x.x?  
**A:** Depends, I'm interested in supporting multiple MC versions, but can't support all of them.  
If you want support for specific MC version pls create an [issue on GitHub](https://github.com/MikiP98/HumilityAFM/issues)

**Q:** Can you port to mod to forge?
**A:** I wanted this to be a forge mod, but I just could get even the empty template to work and I gave up.  
I'm not planing on porting the mod myself, but if you are interested in porting it yourself, you can, pls let me know of such projects on Github :)  
You can also use the mod [Kilt](https://github.com/KiltMC/Kilt) to run forge mods on fabric

**Q:** Why don’t I see *Light Strips* / *Coloured Torches* / *Coloured Jack o'Lanterns* in my game?  
**A:** You need to enable coloured feature set in the config and restart the game. The coloured torches are in beta, and I don't want to separate any of them from each other

**Q:** Why don’t my *Light Strips* / *Coloured Torches* / *Coloured Jack o'Lanterns* emit coloured light?  
**A:** For coloured lighting you need a separate compatible mod or shader. For now this includes [**Bliss** shader](https://github.com/MikiP98/Bliss-Shader) and [**Shimmer** mod](https://modrinth.com/mod/shimmer!)  
  If the light sources still don't emmit coloured light:  
  When using Shimmer mod, there is probably some mod incompatibility. Try disabling the 'NVIDIUM' mod if present and it should start working again, if 'NVIDIUM' is not the causem try asking in Shimmer DC server for help  
  When using Bliss shader, make sure to load it via [Iris](https://modrinth.com/mod/iris) or [Oculus](https://modrinth.com/mod/oculus) and __***not***__ using **Optifine**! Then make sure floodfill option is enabled in the shader settings

**Q:** Why is the inside of Cabinets transparent?  
**A:** Sodium <0.6.0 has issues with rendering transparency. You can try replacing it with Embeddium or installing other mods, like NVIDIUM, may help fix the problem. If that doesn't help, you can disable partial Cabinet transparency in the configuration (you will still be able to see the items inside)

**Q** Why do the edges become transparent when I look at a Cabinet?  
**A:** I have no idea... probably another transparency issue :/

<br>


*Checkout my [other mods](https://modrinth.com/user/Miki-Liki) :)*