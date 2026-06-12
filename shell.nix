{ pkgs ? import <nixpkgs> {} }:

    pkgs.mkShell {
      buildInputs = [
        # Put the exact tools you need here
        pkgs.jdk17        # The Java 17 Development Kit
        pkgs.maven        # If you need Maven
        # pkgs.gradle     # Uncomment if you need Gradle instead
      ];

      shellHook = ''
        echo "Java Development Environment Loaded!"
        java -version
      '';
    }
