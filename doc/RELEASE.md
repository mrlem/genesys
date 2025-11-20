# Release process

1. ensure readme is up to date for changes
2. bump version number to 1.0.8 in `build.gradle.kts`
3. Commit changes:
    ```bash
    git add -p
    git commit -m "build: bump version to 1.0.8"
    git push
    ```
4. Create tag:
    ```bash
    git tag v1.0.8
    git push origin tag v1.0.8
    ```
5. Create builds:
    ```bash
   ./gradlew assembleDist
   ```
6. Draft new release on github:
   - **tag**: v1.0.8
   - **title**: Version 1.0.8
   - generate **release notes**
   - prepend most **notable features**
   - **upload** tar and zip from build/distributions as assets
   - save the release draft to check everything is ok
   - publish the draft release
