{ pkgs ? import <nixpkgs> { } }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    nodejs_22
    docker
  ];

  shellHook = ''
    npm run dev
  '';
}
