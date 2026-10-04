## Building 
Run `gradlew dep` to create your copy of the mod that can be found in `build/libs`. You need jdk 17 installed.
- If this is your first compilation or the `assets/sprites` folder has not been created. this will automatically run `gradlew tools:run`. For following compilations, you must run `gradlew tools:run` manually if you want to add or change sprites.
- For android compatible compilation, follow the instructions on [Anuke's example java mod](https://github.com/Anuken/MindustryJavaModTemplate)

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
3. After the `mindustry.json` file is properly set up, run `gradlew install` to compile the mod and put it into the folder specifien in `savePath`.
4. Running `gradlew runGame` now should open the game with its save file set to what was put in `savePath`. Along with your copy of the mod.
- You can add other mods in your `savePath`, and they will also load during step 4. Keep in mind that if the game crashes, the crash log will still be in `C:Users\<you>\AppData\Roaming\Mindustry\crashes`

## Audio Credits

### Musics
- Pendahuluan - 12Three7
- Pendaratan Pertama - 12Three7
- Terlahir Kembali - 12Three7 
- (note, I've lost contact with 12three7 for a while, and these musics were originally intended for the mod Project Oblivion)
- Ignição - [Lord.dev](https://github.com/lordDev01)

### Sounds
- helicopter - modified for looping, original in [Pixabay](https://pixabay.com/sound-effects/film-special-effects-helicopter-sound-41975/)
- chain - spliced, original in [Pixabay](https://pixabay.com/pt/sound-effects/dom%C3%A9stico-chains-6909/) (unsure if it is the correct link)
- welding - Modified for looping, original in [Pixabay](https://pixabay.com/sound-effects/film-special-effects-lassen-35052/)
- shoot pressure chain, Made by [Cirrus](https://github.com/CirrusIsAkey)
- hit pressure chain, Made by [Cirrus](https://github.com/CirrusIsAkey)