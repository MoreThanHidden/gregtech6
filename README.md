This is a Fork of Gregtech 6 for Hidden's GregTech-6 Port to 1.15.2

This will be a tedious port from 1.7.10 to 1.15.2 that I'll work on from time to time in my spare time.

Thanks to Greg for all the work that has gone into GT6 and to the other contributors, I'll attempt to bring this experience to the newer versions of Minecraft.

# License

This Mod is licensed under the [GNU Lesser General Public License](LICENSE).
All assets, unless otherwise stated, are dedicated to the public domain
according to the [CC0 1.0 Universal Public Domain Dedication](src/main/resources/LICENSE.assets).
Any assets containing the [GregTech logo](src/main/resources/logos) or any
derivative of it are licensed under the
[Creative Commons Attribution-NonCommercial 4.0 International Public License](src/main/resources/LICENSE.logos).

# Support

You can use the Issue Tracker on this branch, don't bug Greg with crap I've broken.

# Dev Environment Setup

IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder
   or run "gradlew eclipse" to generate the project.
