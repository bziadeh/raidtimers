
# Raid Timers

<p align="center">
    <img src="https://svgshare.com/i/c0M.svg" height=183 width=885 />
</p>

<p align="center">
A highly-configurable raid timer plugin optimized for large faction servers.
</p>

## Features

- Multiple version support, utilizing the [XSeries](https://www.github.com/CryptoMorin/XSeries) library.
- Support for [FactionsUUID](https://github.com/drtshock/Factions/), [FactionsX](https://www.spigotmc.org/resources/factionsx.83459/), [SaberFactions](https://github.com/SaberLLC/Saber-Factions), and more.
- Blocks the placement, destruction, or use of certain items and blocks during raid.
- Prevents multiple factions from raiding the same base simultaneously.
- Explosion shield given to defenders after the raid ends.
- Built-in raid interface, displaying current raid info in real-time.



## API Usage/Examples

#### Start by getting a reference:
```java
    RaidApi api = RaidTimers.getInstance().getApi();
```
#### How to get a raid in progress:
```java
    RaidContext raid = api.getRaidInProgress(faction);
```
#### How to initiate a raid between two factions:
```java
    api.setRaidInProgress(attacker, defender);
```

## Configuration File
```yml
# If you start a raid, and stop shooting for 'x minutes or hours' then,
# the defending faction will receive a shield.
explosion-threshold: 15 minute

# This is how long the shield will last after a faction
# has successfully defended a raid.
shield-duration: 15 minutes

# Prevent the placement of the following blocks during raid defense.
prevent-placement:
- TNT
- OBSIDIAN
- COBBLESTONE

# Prevent the destruction of the following blocks during raid defense.
prevent-break:
- MOB_SPAWNER

# Prevent the use of the following items during raid defense.
# Note: Here you should place the item type of your genbuckets.
prevent-use:
- CREEPER_SPAWN_EGG
- BUCKET

# Prevent the use of certain commands (by the defenders) during raid.
prevent-commands: []
```

## Screenshots and GIFs
<br>
<p align="center">Raid Interface</p>
<p align="center">
  <img src="https://i.gyazo.com/d3e498bb0c835ff42d7b6e6a5680570e.gif" />
</p>
