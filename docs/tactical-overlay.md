# The tactical overlay

<div align="center">

[tactical-overlay.png]: images/tactical-overlay.png

[![A screenshot of the tactical overlay.][tactical-overlay.png]][tactical-overlay.png]

<sup>All colors of hatching displayed by the tactical overlay.</sup>

</div>

The *tactical overlay* is one of the most important parts of the user interface to pay attention to during a game, as it provides crucial information about what might happen on the current turn based on your currently-planned orders, especially in games using the Classic ruleset. It appears as hatching in one of four colors superimposed upon system hexes. This hatching is displayed at all zoom levels, but it is significantly easier to see when fully zoomed out (as depicted in the picture above).

The different colors in the tactical overlay have the following meanings:

* **Green** hatching indicates systems you are guaranteed to capture.

* **Yellow** hatching indicates systems you might capture or might lose.

* **Orange** hatching is like yellow hatching, but it only appears on systems you already control, and it additionally indicates that the systems may be attacked by a neighboring hostile system.

* **Red** hatching indicates systems you are guaranteed to lose.

When playing with the Classic (rather than Streamlined) ruleset, system collapses due to insufficient garrison can cause chain reactions that result in large numbers of systems being lost. To distinguish the key systems that may start such chain reactions, yellow and red hatching comes in two variants for systems you control:

* On systems you control, **bright yellow** and **bright red** hatching indicate systems that are at *immediate risk* of collapsing this turn due to insufficient garrison, ignoring chain reactions.

* **Dim yellow** and **dim red** hatching indicate systems that are at risk of collapsing this turn due to a chain reaction of other collapses.

The tactical overlay updates automatically as you modify your orders, so it can be useful to experiment with different tentative fleet movements to get an intuition for what the different colors mean.

## Using the tactical overlay effectively

The complex garrison rules in the Classic ruleset make the tactical overlay an indispensable resource when planning your turn. As long as you pay attention to the hatching and respond appropriately, it is *impossible* to lose your whole empire to a catastrophic chain reaction of system collapses.

**Orange** hatching indicates systems that you have some chance of losing this turn regardless of their garrison. Building or moving fleets into them reduces the chance that they will be captured next turn, but it cannot completely eliminate the possibility. Therefore, there is no “correct” number of fleets to place in these systems, and you must determine their garrison based on what you think other players will do on the next turn and how valuable they are to hold.

In contrast, **yellow** and **red** hatching on systems you control can always be removed by altering system garrisons. In particular, **yellow** hatching can be eliminated by increasing the garrisons of systems with **bright yellow** hatching, and **red** hatching can be eliminated by increasing the garrisons of systems with **bright red** hatching. Note that there is *no* advantage to increasing the garrisons of systems with **dim yellow** or **dim red** hatching, as these systems can only collapse if the systems with bright hatching do.

One important property that follows from the above is that there is almost always a specific, optimal number of fleets to leave garrisoned in each system in your empire that does *not* have **orange** hatching. If a fleet can be moved out of an unhatched system without causing the system to become hatched, it probably *should* be moved, as it can likely be put to better use elsewhere. The only exception to this is if you intend to mount an attack from that system on the following turn but cannot do so on the current turn, due to either an alliance that is about to expire or a Tannhäuser project you are about to deploy.

### The tactical overlay and stellar bombs

Because stellar bombs prevent all fleet movements out of the target system for a turn, deploying a stellar bomb can affect the tactical overlay. Most significantly, if a system you control is only bordered by one hostile system, deploying a stellar bomb on that system will eliminate the **orange** hatching on that system! This is in fact the *only* way to remove such hatching, so although it may be counterintuitive, the most valuable way to use a stellar bomb may be defensive rather than offensive.
