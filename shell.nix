{ pkgs ? import <nixpkgs> { } }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    gradle
    jdk17
    docker
  ];

  shellHook = ''
    gradle clean build -x test
    docker compose up --build
    exit
  '';
}
