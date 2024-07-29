{ pkgs ? import <nixpkgs> { } }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    gradle
    jdk17
  ];

  shellHook = ''
    gradle bootRun
    exit
  '';
}
