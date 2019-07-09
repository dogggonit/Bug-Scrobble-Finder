# Bug-Scrobble-Finder
Tool to find Last.FM scrobbles that were made incorrectly by SubSonic

### Use of this Tool
If you use last.fm scrobbling with SubSonic or one of its derivatives it may have created improper scrobbles referenced by [this](https://github.com/airsonic/airsonic/issues/776) bug.

### How to Use
Clone the repository
1. Clone the repository: `git clone `
2. Enter the directory: `cd Bug-Scrobble-Finder`
3. Build the jar file: `mvm clean package`
4. Run it: `java -jar Bug-Scrobble-Finder-*.jar`

-or-

Download it from [releases]().

### Notes
Since last.fm's api does not provide any methods to delete scrobbles you will have to manually go through and delete them by hand. however this tool will try to determine if a scrobble is good or bad for you and tell you what page it is on.
