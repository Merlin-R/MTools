MTools
-----------------------------------------------------------------
MTools is an advanced tool and item creation plugin for Spigot by **Merlin Reichwald (Venesus)** for the Chroma Hills Server.

# Features
MTools offers a vast amount of configuration options:

## General Features
- Create custom tools or items
- Define custom crafting recipes
- Define permission nodes for each item action ( craft / use / etc )
- Create tools with inventories

## Attach to Various Events
- React to _leftclick_ and _rightclick_ for different targets ( _Entities_, _Blocks_, _Air_ )
- Tool events, such as durability taken, item crafted, item destroyed
- Special events like _block breaking_ or _attacking_

## Apply Custom Actions
- Execute commands
- _break_ or _place_ blocks in different _patterns_
  - use player inventory when in survival
- _damage_ or _heal_ surrounding entities
- apply potion effects
- consume internal items


# Configuration

Once you have downloaded the plugin and dropped it into your server directory,
a new folder called `MTools` should have generated.

Inside this folder, there will be another folder called `Tools`.

Every file inside that folder will be loaded and treated as a `YAML` Tool Configuration.

## HOW THOSE FILES SHOULD BE STRUCTURED IS NOT YET SET IN STONE, THEREFORE NO FURTHER DOCUMENTATION YET

# General
## TODOs
- Almost everything but block breaking and listening for events. Will update later.

## CHANGELOG
29.08.2016 - Created github repository.
