## 0.0.7 (not yet released)

* Several improvements have been made to the tactical overlay:

  * Systems that are threatened by a neighboring hostile force are now overlaid with orange hatching instead of yellow hatching. Yellow hatching is now used exclusively for systems at risk of collapse due to insufficient garrison.

  * The bright yellow hatching used to indicate systems at risk of immediate collapse is now applied more consistently and accurately. Previously, some systems could be shaded with faded yellow hatching even though they were at risk of immediate collapse, but now dim yellow hatching is only applied to systems at risk of collapse due to a chain reaction.

  * Fleet movements out of systems that are at risk of being stellar bombed no longer count towards the worst-case garrison calculation and therefore will not eliminate yellow or red hatching on their destination system. With this change, the absence of any hatching on a system provides a true guarantee that the system will not be lost.

* While placing a terraforming project, resources that are currently in surplus are colored green in the production panel, and systems that only provide a single unit of that resource (and provide no other resources) are highlighted green on the map.

* A few minor bugfixes.

## 0.0.6 (2022-12-04)

* Added experimental support for custom UI scaling via the `funorb.shatteredplans.client.uiScale` and `funorb.shatteredplans.client.aspectRatio` JVM properties. See the README for more details.

* Fixed game chat messages being sent as lobby chat messages.

## 0.0.5 (2022-10-01)

* Initial public release.
