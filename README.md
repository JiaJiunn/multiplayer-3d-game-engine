# Multiplayer 3D Game Engine
A simple 3D game engine written in Java using OpenGL, with multiplayer support via sockets.

## Installation

This game engine currently requires the following Java libraries:

- LWJGL 2
- Slick-Util 
- PNGDecoder

The JAR files and natives for these libraries are already provided under `lib`. If you are using Eclipse, you can simply right-click the project, and head to `Build Path > Configure Build Path`. Then, under `Libraries`, click `Classpath > Add JARs` and add the JAR files under `lib/jar/`. Additionally, click `JRE System Library > Native Library Location > Edit` and attach the directory `lib/natives/`.

## Quick Start

First, run `src/server/PositionServer.java` to start the server. You can then run `src/main/Main.java` any number of times for spawning clients to join the server. If you're joining the server from a different computer, be sure to update the IP and port number of the server under `src/main/Main.java` correspondingly.

The end results should be something like follows:

<figure class="video_container" align="center">
  <video controls="true" width="700" allowfullscreen="true">
    <source src="assets/AppDemo.mp4" type="video/mp4">
  </video>
</figure>

## Credits

I wrote this project mainly as a way to learn about OpenGL and socket programming. I obtained the 3D models from [here](https://www.dropbox.com/sh/i76qpo3ug7vvq98/AADQtfO_WsUcU_cEroaqKd9ya?dl=0), and was inspired a lot by [ThinMatrix's online tutorial series](https://www.youtube.com/watch?v=VS8wlS9hF8E&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP).
