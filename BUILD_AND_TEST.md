# Building and Testing Your Fabric Mod

## Building the Mod

### Build the mod JAR file
To compile and build your mod into a JAR file:

```bash
./gradlew build
```

This will:
- Compile your Java code
- Process resources
- Create a remapped JAR file in `build/libs/`
- The final mod JAR will be named something like `mc-dimensions-1.0.0.jar`

### Build without running tests
If you want to skip tests (useful for faster builds):

```bash
./gradlew build -x test
```

### Clean build
To clean previous build artifacts and rebuild:

```bash
./gradlew clean build
```

## Testing the Mod

### Run Minecraft Client (Recommended for testing)
To launch Minecraft with your mod loaded in a development environment:

```bash
./gradlew runClient
```

This will:
- Download Minecraft assets if needed
- Launch the Minecraft client with your mod loaded
- Allow you to test your mod in-game
- Hot-reload changes (after stopping and restarting)

### Run Minecraft Server
To test your mod on a dedicated server:

```bash
./gradlew runServer
```

This launches a Minecraft server with your mod loaded.

### First-time setup
On the first run, you may need to:
1. Accept the Minecraft EULA (if running server)
2. Wait for assets to download
3. Configure launch settings

## Development Workflow

1. **Make changes** to your code
2. **Build** with `./gradlew build`
3. **Test** with `./gradlew runClient` or `runServer`
4. **Iterate** - stop the game, make changes, rebuild, and test again

## Output Files

- **Mod JAR**: `build/libs/mc-dimensions-1.0.0.jar` (or current version)
- **Sources JAR**: `build/libs/mc-dimensions-1.0.0-sources.jar`
- **Remapped JAR**: `build/libs/mc-dimensions-1.0.0.jar` (ready for distribution)

## Troubleshooting

### Java Home Configuration Error
If you see an error like:
```
The java.home variable defined in Cursor settings points to a missing or inaccessible folder
```

**Solution:**
1. Find your Java installation path:
   ```bash
   dirname $(dirname $(readlink -f $(which java)))
   ```
   On this system, it's `/opt/java/openjdk`

2. Update Cursor settings:
   - Open Cursor Settings (Ctrl+, or Cmd+,)
   - Search for "java.home" or "java configuration"
   - Update the `java.home` setting to: `/opt/java/openjdk`
   - Or set it via settings.json:
     ```json
     "java.home": "/opt/java/openjdk"
     ```

3. Restart Cursor for changes to take effect

**Note:** Gradle will use the system Java automatically, so this mainly affects IDE features like IntelliSense and debugging.

### Out of memory errors
If you encounter memory issues, increase the JVM memory in `gradle.properties`:
```
org.gradle.jvmargs=-Xmx2G
```

### Build fails
- Check that you have Java 21 installed: `java -version`
- Verify Java is accessible: `which java`
- Run `./gradlew clean` and try again
- Check the error messages in the build output

### Mod doesn't load
- Verify `fabric.mod.json` is correct
- Check that all dependencies are properly declared
- Look at the game logs for errors

## Additional Useful Commands

- **View dependencies**: `./gradlew dependencies`
- **Generate IDE files**: `./gradlew vscode` or `./gradlew eclipse`
- **Validate access widener**: `./gradlew validateAccessWidener`
- **Generate sources**: `./gradlew genSources` (decompiled Minecraft sources)

