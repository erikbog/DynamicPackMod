# Dynamic Pack Mod

Fabric server mod for Minecraft `26.2` that replaces the resource-pack SHA-1 sent to each joining client with a value fetched from an HTTP endpoint.

## Setup

1. Install Fabric Loader `0.19.5` and Fabric API `0.160.0+26.2` on the server.
2. Copy the built jar (`build/libs/dynamic-pack-mod-1.0.0+26.2.jar`) into the server's `mods` directory.
3. Start the server once. Edit `config/dynamic-pack-mod.properties` and set:

	```properties
	hashEndpoint=https://example.com/resource-pack.sha1
	```

4. Restart the server.

The endpoint must return only the lowercase or uppercase 40-character hexadecimal SHA-1 hash as plain text. The resource-pack URL and other pack settings still come from the server's normal resource-pack configuration.

The request has five-second connection and response timeouts. If the endpoint fails or returns an invalid value, the pack is sent with an empty hash rather than reusing the hash from `server.properties`.

## Configuration

Configure your resource pack URL in `server.properties`:

```properties
resource-pack=https://example.com/resource-pack.zip
```

The mod will automatically override the SHA-1 hash sent to connecting players while preserving the pack ID so clients can take advantage of local disk caching.

## Build

Requires Java 25:

```bash
./gradlew build
```
