# Lighty Client (Fabric 1.21.11)
Autor: L1ghtyyy

- Prawy Shift - menu moda (przelaczniki modulow)
- R - tracery (linie od celownika do graczy)
- N - nametagi (nick, HP, dystans)
Klawisze zmienisz na gorze pliku LightyClient.java.

## Budowanie bez instalowania czegokolwiek (GitHub Actions)
1. Zaloz konto na github.com i nowe repozytorium (np. lighty-client).
2. Wgraj do niego WSZYSTKIE pliki z tego zipa (po rozpakowaniu): Add file > Upload files.
3. Jesli folder .github sie nie wgral: Add file > Create new file, wpisz nazwe
   `.github/workflows/build.yml` i wklej zawartosc pliku z zipa.
4. Zakladka Actions > "build" > poczekaj na zielony znaczek > na dole "Artifacts" > lighty-client (zip z jarem).
5. Jar wrzuc do folderu mods (Fabric Loader 0.18.x + Fabric API 1.21.11).

## Lokalnie
JDK 21 + IntelliJ IDEA: otworz folder jako projekt Gradle i uruchom zadanie `build`.
