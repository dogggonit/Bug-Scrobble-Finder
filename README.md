# Bug-Scrobble-Finder
Tool to find Last.FM scrobbles that were made incorrectly by SubSonic

### Why this Tool Exists
If you use last.fm scrobbling with SubSonic or one of its derivatives it may have created improper scrobbles referenced by [this](https://github.com/airsonic/airsonic/issues/776#issuecomment-455806900) bug.  After scrobbling since 2016 with SubSonic I didn't want to have to go through my scrobble history manually and so I created this tool.
This bug only effects songs scrobbled from the web player, if you only use an app to listen to your SubSonic library then you shouldn't need this tool.

### Requirements
1. A last.fm with many repetitive scrobbles
2. A SubSonic library with the scrobbled songs in it
3. A last.fm api [key](https://www.last.fm/api/account/create)
4. Patience

### How to Use
1. Clone the repository: `git clone `
2. Enter the directory: `cd Bug-Scrobble-Finder`
3. Build the jar file: `mvm clean package` (the build package will be in the target/ directory)
4. Run it: `java -jar Bug-Scrobble-Finder-*.jar`

-or-

Download it from [releases](https://github.com/GYBATTF/Bug-Scrobble-Finder/releases).

### Notes
Since last.fm's api does not provide any methods to delete scrobbles you will have to manually go through and delete them by hand. however this tool will try to determine if a scrobble is good or bad for you and tell you what page it is on.
