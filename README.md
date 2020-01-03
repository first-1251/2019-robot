# 2019 - Infinite Recharge - Sentinel

This is the public release of TEAM 1251's code for their 2019 FRC Robot: `Sentinel`.

## About This Repo

This is a copy of the 2019 season's code that has gone through heavy refactoring. The goal of the refactor was to 
improve documentation, reduce clutter, and flush out some half-baked ideas in the foundational code that emerged
throughout the season.

The hope is that this version is easier to understand so that it can be better used as an instructional tool. 

The secondary goal of this refactor is to provide a clean(er) foundation for the 2020 Code, allowing the team to focus
more on what matters -- the Robot's behavior logic.

## What was (and was not) Changed

All of the refactored pieces were "foundational" rather than "functional". For example, classes that were used to
read input from an Xbox controller were refactored, but the logic for interpreting that input was not.

Commands and Subsystems were left as-is except for in areas where there were unused methods or was commented out code
since these are just distractions from the meaningful parts. 

At the end of the day, this is a faithful representation of the logic used by Sentinel to help earn our Orlando Regional
Winning Alliance banner. But readers should, by no means, think that our code was this well maintained during the chaos
of the build season!

## Off-Season

We were fortunate enough to participate in a "football-launcher" competition at the FAU Cheribundi Bowl during the 2019
off-season. This great opportunity was brought to us by one of our wonderful sponsors, Florida Power and Light (FPL).
To see the code that powered our winning design, check out the `football-launcher` branch!

## On to 2020!

Good luck to all teams in the 2020 competition season! This code is graciously donated to the community, feel free to
use any part of it in your 2020 Robot (attribution would be appreciated). We will keep the `Issues` section open and
do our best to answer any questions!
