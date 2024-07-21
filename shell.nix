{ pkgs ? import <nixpkgs> { } }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    gradle
    jdk17
    docker
  ];

  shellHook = ''
    gradle bootRun
    #gradle clean build -x test
    #java -jar nms.jar
    exit
  '';
}
