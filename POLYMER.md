# HumilityAFM for Polymer

This is a server side Polymer port of the [HumilityAFM](https://modrinth.com/mod/humility-afm) mod.  
Polymer allows mods to work purely server side with no mod being required on the client.  
If you want to know what HumilityAFM is, please see the original's mod description.

**IT IS RECOMMENDED THOUGH TO USE THE ORIGINAL NON-SERVER SIDE ONLY MOD WHEN POSSIBLE**

Due to Polymer's mod limitations, this is quite a compromised experience compared to the full mod, these include:
- Possible incorrect block lighting and shading
- Incorrect hitboxes
- Lower server and client performance
- Missing or incorrect sounds
- Buggy interactions
- Worse resourcepack compatibility
- No creative tabs *(without the mod on the client)*

The issues are far less impactful on MC versions 1.21+, and even less so on 26+, 
but they still exist and it isn't possible to interact with the mod in any way without running into them.

<br>

### Server Configuration

Server owners can use `-Dhumilityval.polymer.attachment_per_tick_limit={int}` JVM flag *(default: 500)* 
to change the maximal amount of Polymer's VirtualBlockEntities that can be loaded-in in a single tick.  
If server lags too much during chunks loading this limit can be decreased.  
And the other way if some mega build loads-in too slow, this value can be increased.  
*Example usage: `-Dhumilityval.polymer.attachment_per_tick_limit=100`*