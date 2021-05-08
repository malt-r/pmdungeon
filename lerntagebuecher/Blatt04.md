---
title:  'Lerntagebuch zur Bearbeitung von Blatt 04'
author:
- MALTE REINSCH (malte.reinsch1@fh-bielefeld.de)
- DENNIS ELLER (dennis.eller@fh-bielefeld.de)
- TIM LUECKING (tim.luecking2@fh-bielefeld.de)
  ...

<!--
Führen Sie zu jedem Aufgabenblatt und zum Projekt (Stationen 3-9) ein
Lerntagebuch in Ihrem Team. Kopieren Sie dazu diese Vorlage und füllen
Sie den Kopf entsprechend aus.

Im Lerntagebuch sollen Sie Ihr Vorgehen bei der Bearbeitung des jeweiligen
Aufgabenblattes vom ersten Schritt bis zur Abgabe der Lösung dokumentieren,
d.h. wie sind Sie die gestellte Aufgabe angegangen (und warum), was war
Ihr Plan und auf welche Probleme sind Sie bei der Umsetzung gestoßen und
wie haben Sie diese Probleme gelöst. Beachten Sie die vorgegebene Struktur.
Für jede Abgabe sollte ungefähr eine DIN-A4-Seite Text erstellt werden,
d.h. ca. 400 Wörter umfassen. Wer das Lerntagebuch nur ungenügend führt
oder es gar nicht mit abgibt, bekommt für die betreffende Abgabe 0 Punkte.

Checken Sie das Lerntagebuch mit in Ihr Projekt/Git-Repo ein.

Schreiben Sie den Text mit [Markdown](https://pandoc.org/MANUAL.html#pandocs-markdown).

Geben Sie das Lerntagebuch stets mit ab. Achtung: Wenn Sie Abbildungen
einbetten (etwa UML-Diagramme), denken Sie daran, diese auch abzugeben!

Beachten Sie auch die Hinweise im [Orga "Bewertung der Aufgaben"](pm_orga.html#punkte)
sowie [Praktikumsblatt "Lerntagebuch"](pm_praktikum.html#lerntagebuch).
-->


# Aufgabe

<!--
Bitte hier die zu lösende Aufgabe kurz in eigenen Worten beschreiben.
-->
## HUD
## Erfahrung und Skills
## Fallen
Der letzte Teil des Praktikums war die Implementierung von Fallen. Dabei sollen Fallen durch Gegenstände, Monster oder
durch den Helden ausgelöst werden. Die Fallen sollen zum Beispiel Schadenspunkte verteilen, den Auslöser teleportieren 
oder zufällige Monster herbeizaubern. Dabei können Fallen sichtbar oder versteckt sein. Versteckte Fallen sollen über 
einen Zaubertrank oder Zauberspruch sichtbar gemacht werden können. Des Weiteren sollen Fallen entweder einmal aktiviert
werden oder mehrmals aktiviert werden können.
# Ansatz und Modellierung

<!--
Bitte hier den Lösungsansatz kurz beschreiben:
-   Wie sollte die Aufgabe gelöst werden?
-   Welche Techniken wollten Sie einsetzen?
-   Wie sah Ihre Modellierung aus (UML-Diagramm)?
-   Worauf müssen Sie konkret achten?
-->
## HUD
## Erfahrung und Skills
## Fallen

Als Fallen soll einmal eine HoleTrap (Loch in das ein Actor fallen kann und stirbt), eine SpikeTrap (Stacheln die
kontinuierlich ein und ausfahren), die unsichtbar ist und erst durch einen Zauberspruch sichtbar gemacht werden können 
und eine ActivatorTrap (Beim Ablegen einer Gegenstands auf den Altar werden Monster gespawnt).
Da es verschiedene Fallenarten gibt werden die Fallen über die TrapFactory erzeigt. Dadurch wird das Erstellen von 
Fallen vereinfacht. Damit es (wie bei der Verwendung eines Strings) zu keinen Tippfehlern kommen kann, wird ein TrapType
Enum definiert, welches alle möglichen Fallenarten definiert. Dabei enthält LevelContent Informationen über ein
spezifisches Level (Monster, Item, Kisten etc.) während LevelInfo die Informationen über alle Level hält und dem 
GameController zur Verfügung stellt.


# Umsetzung

<!--
Bitte hier die Umsetzung der Lösung kurz beschreiben:
-   Was haben Sie gemacht,
-   an welchem Datum haben sie es gemacht,
-   wie lange hat es gedauert,
-   was war das Ergebnis?
-->

## HUD
## Erfahrung und Skills
## Fallen
Durch die immer größer werdenen Anzahl an Entitätsarten wurde ein Spawner implementiert, der alle Factory-Objekte
zusammenfasst und dem GameController zur Verfügung stellt. Des Weiteren werden die Informationen über die Level über die
Klassen LevelInfo und LevelContent.
Die SpikeTrap wurde mit einer Statemachine implementiert, die über SpikesTrapState die Zustände wechselt. Dabei teilt 
die SpikesTrap bei jeder Aufwärtsbewegung der Stacheln (sobald diese über der Oberfläche sind) Schaden an alle Actor 
Instanten aus. SpikeTraps sind für den Helden nicht sichtbar. Erst wenn eine SupervisionScroll verwendet wird, kann die
Falle im Level gesehen werden.
Die HoleTrap tötet jede Entity die in das Loch hinein gerät. Läuft der Spieler in das Loch, ist das Spiel beendet 
(Game Over). Die ActivatorTrap spawnt drei zufällige Monster, wenn ein Gegenstand auf sie abgelegt wird.

07.05.2021 14:00 – 22:00 Erste Version Fallen, Spawner
08.05.2021 8:00 – 20:00 Factories für alle Entities erstellt, ActivatorTrap, Anpassungen der Skalierungen

# Postmortem
<!--
Bitte blicken Sie auf die Aufgabe, Ihren Lösungsansatz und die Umsetzung
kritisch zurück:
-   Was hat funktioniert, was nicht? Würden Sie noch einmal so vorgehen?
-   Welche Probleme sind bei der Umsetzung Ihres Lösungsansatzes aufgetreten?
-   Wie haben Sie die Probleme letztlich gelöst?
-->
## HUD
## Erfahrung und Skills
## Fallen
Das Verwenden einer Basisklasse vereinfacht die Implementierung der Fallen erheblich. Bei der Implementierung erwies es 
sich problematisch, dass noch kein Verständnis dafür vorlag, wie der offset von der Engine verarbeitet wird, was zu viel
Rumprobieren geführt und viel Zeit gekostet hat. Des Weiteren war es problematisch zu überprüfen, ob ein Actor bereits 
von einer Falle getroffen wurde. Um das zu Lösen werden Schadenspunkte nur beim Zustandswechsel der Falle abgezogen.