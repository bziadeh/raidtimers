
# Raid Timers

A highly-configurable, optimized Raid Timer plugin built for large faction servers.

![Logo](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/th5xamgrr6se0x5ro4g6.png)


## Features

- Multiple version support, utilizing the [XSeries](https://www.github.com/CryptoMorin/XSeries) library.
- Support for [FactionsUUID](https://github.com/drtshock/Factions/), [FactionsX](https://www.spigotmc.org/resources/factionsx.83459/), [SaberFactions](https://github.com/SaberLLC/Saber-Factions), and more.
- Ability to block the placement, destruction, or use of certain items/blocks during raid.
- Ability to prevent multiple factions from raiding the same base simultaneously.
- Shield given to defenders after the raid ends.
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

## Screenshots and GIFs
![](https://i.gyazo.com/d3e498bb0c835ff42d7b6e6a5680570e.gif)