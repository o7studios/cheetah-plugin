package studio.o7.cheetah.plugin.api.player.bedrock;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum Device {
    UNKNOWN("Unknown"),
    GOOGLE("Android"),
    IOS("iOS"),
    OSX("macOS"),
    AMAZON("Amazon"),
    GEARVR("Gear VR"),
    HOLOLENS("Hololens"),
    UWP("Windows"),
    WIN32("Windows x86"),
    DEDICATED("Dedicated"),
    TVOS("Apple TV"),
    PS4("PlayStation"),
    NX("Switch"),
    XBOX("Xbox"),
    WINDOWS_PHONE("Windows Phone"),
    LINUX("Linux");

    private final String name;

    public static Optional<Device> getDeviceByName(@NonNull String name) {
        return Arrays.stream(values()).filter(device -> device.name.equalsIgnoreCase(name)).findAny();
    }
}
