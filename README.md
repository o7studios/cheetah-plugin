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

Please check out [Development Container](.devcontainer/README.md) for project setup.
