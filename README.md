
## Building for Desktop Testing

1. Install JDK **17**.
2. Run `gradlew jar` [1]. or `./gradlew jar` in Linux/Mac
3. Your mod jar will be in the `build/libs` directory. **Only use this version for testing on desktop. It will not work with Android.**
To build an Android-compatible version, you need the Android SDK. You can either let GitHub Actions handle this, or set it up yourself. See steps below.

## Building through GitHub Actions

This repository is set up with GitHub Actions CI to automatically build the mod for you every commit. This requires a GitHub repository, for obvious reasons.
To get a jar file that works for every platform, do the following:
1. Make a GitHub repository with your mod name, and upload the contents of this repo to it. Perform any modifications necessary, then commit and push. 
2. Check the "Actions" tab on your repository page. Select the most recent commit in the list. If it completed successfully, there should be a download link under the "Artifacts" section. 
3. Click the download link (should be the name of your repo). This will download a **zipped jar** - **not** the jar file itself [2]! Unzip this file and import the jar contained within in Mindustry. This version should work both on Android and Desktop.

## Building Locally

Building locally takes more time to set up, but shouldn't be a problem if you've done Android development before.
1. Download the Android SDK, unzip it and set the `ANDROID_HOME` environment variable to its location.
2. Make sure you have API level 30 installed, as well as any recent version of build tools (e.g. 30.0.1)
3. Add a build-tools folder to your PATH. For example, if you have `30.0.1` installed, that would be `$ANDROID_HOME/build-tools/30.0.1`.
4. Run `gradlew deploy`. If you did everything correctly, this will create a jar file in the `build/libs` directory that can be run on both Android and desktop. 

## Processing Assets

1. Place the unprocessed sprites in the assets/sprites-raw folder
2. Run `gradlew tools:run`
3. Your packed sprites will be on the assets/sprites folder, keep in mind that this folder will not be pushed to github.

## Running the game with the mod

To automatically process, copy file into mod folder and run mindustry, do the following:
1. add a shortcut to your copy of mindustry into the mod folder, only .lnk files are supported and it has to be named "app"
2. Run `gradlew toors:run install rungame`
3. `tools:run` will process sprites
4. `install` will move the built jar from build/libs to /appdata/mindustry/mods
5. `rungame` will execute the app.lnk file in the root project folder, if no app.lnk file is there, it will cause an error

## Adding Dependencies

Please note that all dependencies on Mindustry, Arc or its submodules **must be declared as compileOnly in Gradle**. Never use `implementation` for core Mindustry or Arc dependencies. 

- `implementation` **places the entire dependency in the jar**, which is, in most mod dependencies, very undesirable. You do not want the entirety of the Mindustry API included with your mod.
- `compileOnly` means that the dependency is only around at compile time, and not included in the jar.

Only use `implementation` if you want to package another Java library *with your mod*, and that library is not present in Mindustry already.
