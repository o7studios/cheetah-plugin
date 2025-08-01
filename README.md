<img width="1024" height="220" alt="image" src="https://github.com/user-attachments/assets/de4cf431-881b-483e-b702-f4cef7c8af62" />

# Cheetah Paper plugin

**Paper plugin with API for wrapper around Cheetah SDK for o7studios product Cheetah**

- Runs on [Paper](https://papermc.io/software/paper)

## Usage

Add dependency to plugin:

```kotlin
dependencies {
    compileOnly("studio.o7:cheetah-plugin-api:X.Y.Z")
}
```

Add _depend_ inside `plugin.yml`:

```yaml
depend:
  - Cheetah
```

## Development

Full development setup available as [Development Container](https://containers.dev/).
Please use it for being able to tell "It works on my machine".

**Docker is required to be installed on your machine!**

### IntelliJ IDEA

- Open IntelliJ (Welcome screen)
- Navigate to `Remote Development` - `Dev Containers`
- Press `New Dev Container`
- Select `From VCS Project`
- Select and connect with `Docker`
- Select `IntelliJ IDEA`
- Enter `Git Repository`: `https://github.com/o7studios/cheetah-plugin`
- Select `Detection for devcontainer.json file` `Automatic`
- Press `Build Container and Continue`

### Development Container Issues

If you encounter an issue with setting up a development container, please
try to rebuild it first before opening a GitHub Issue. It's not uncommon
that some issues may fix themselves after a fresh container rebuild.