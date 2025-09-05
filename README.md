
## Building 
You should know how to do it. It's the same as the mod templates, or any java thing tbh.

## Processing Assets

1. Place the unprocessed sprites in the assets/sprites-raw folder
2. Run `gradlew tools:run`
3. Your packed sprites will be on the assets/sprites folder, keep in mind that this folder will not be pushed to github.

## Running the game with the mod

As far as i'm aware, this task is windows only (don't have linux to test that)
1. First run `gradlew createRunDir`, this will generate a run folder inside the project containing a `mindustry.json` file
2. Define the parameters `gamePath` and `savePath` inside the `mindustry.json` file, 
- `gamePath` must be set to the path to an executable of the game, make sure that the executable's name and file extension is included.
- `savePath` is the path to the root folder of the local save that the mod is being tested on. Leave empty to default to `C:\Users\<you>\AppData\Roaming\Mindustry\mods`
3. After the `mindustry.json` file is properly set up, run `gradlew runGame`. The game should open.
