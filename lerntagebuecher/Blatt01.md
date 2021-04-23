---
title:  'Lerntagebuch zur Bearbeitung von Blatt 01'
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

Die zu erfüllende Aufgabe umfasst das Erzeugen eines neuen Java-Projekts, welches
für die kommenden Praktikumsaufgaben genutzt wird. Dazu werden ein Asset-Order
und eine Java-Bibliothek bereitgestellt, welche in das Projekt zu integrieren sind.

Des Weiteren soll die Basis für das zu entwickelnde Spiel geschaffen werden, welche
einen steuerbaren und animierten Spielcharakter und eine zufällig generierte Spielwelt umfasst.

Darüber hinaus soll der automatische Wechsel zwischen einer Idle-Animation und
einer Lauf-Animation implementiert werden. Insbesondere für die Lauf-Animationen
soll je nach Laufrichtung die Blickrichtung der Animation geändert werden.

# Ansatz und Modellierung

<!--
Bitte hier den Lösungsansatz kurz beschreiben:
-   Wie sollte die Aufgabe gelöst werden?
-   Welche Techniken wollten Sie einsetzen?
-   Wie sah Ihre Modellierung aus (UML-Diagramm)?
-   Worauf müssen Sie konkret achten?
-->

Der Ansatz zur weitestgehenden Erfüllung der Aufgaben ist in der Dokumentation
dargestellt, auf die Umsetzung der Dokumentation wird daher nicht vertiefend eingegangen.

Zur Darstellung der Animationsrichtung (entweder links oder rechts)
reicht eine interne boolesche Variable in der Hero Klasse aus. Da zukünftig weitere Animationsarten
(z.B. attackieren oder sterben) unterstützt werden sollen, wird die Animationsart (Idle, Run)
als Enumeration `AnimationState` abgebildet. Durch diese Abbildung kann die Hero-Klasse
zukünftig mit vergleichsweise geringem Aufwand um weitere Animationen ergänzt werden.

# Umsetzung

<!--
Bitte hier die Umsetzung der Lösung kurz beschreiben:
-   Was haben Sie gemacht,
-   an welchem Datum haben sie es gemacht,
-   wie lange hat es gedauert,
-   was war das Ergebnis?
-->

Eine Animation besteht aus mehreren Frames, welche als Textur aus einer .png-Datei
geladen werden. Um das Programm portierbar zu halten und nicht von absoluten
Dateipfaden abhängig zu sein, wird der assets-Order in IntelliJ als Resource-Root
Verzeichnis konfiguriert. Hierdurch wird beim Bauen des Projekts der Inhalt des Assets-Ordners
in das Output-Verzeichnis kopiert. Im Programm kann anschließend über den Aufruf der
`this.getClass().getResource(<relativePath>)` Methode auf eine robustere Art der
Pfad einer Textur gelesen werden, als den absoluten Pfad der Datei zu kodieren.

Es werden insgesamt vier Animationen (Idle links, Idle recht, Run links, Run rechts) erzeugt.
Der Wechsel zwischen den Animationen wird in die update-Methode der Hero-Klasse in Abhängigkeit
der Nutzereingabe (WASD-Tasten) integriert. Um die update-Methode übersichtlich zu halten,
erfolgt das Setzen der aktuellen Animation nicht direkt in den konditionalen Abfragen der einzelnen Tasten.
In diesen Abfragen wird lediglich der Wert der booleschen Variable lookLeft gesetzt,
um die Blickrichtung der Animation zu speichern.

Die Art der Animation wird durch die Enumeration AnimationState dargestellt, welche aktuell
über die Varianten `IDLE` und `RUN` verfügt. Eine Variable vom Typ `AnimationState` wird
in Abhängigkeit der Differenz von der aktuellen `this.position` und der neuen berechneten
Position gesetzt. So kann das Abspielen der Lauf-Animation bei z.B. gleichzeitigem Drücken der A- und D-Tasten
verhindert werden, bei dem sich der Charakter effektiv nicht bewegt.

Die nötige Logik um mit der ermittelte Blickrichtung und Animationsart `currentAnimation` auf den
korrekten Wert zu setzen, wird in der Methode `setCurrentAnimation` implementiert.
Durch die Kapselung dieser Logik in dieser Methode wird die `update`-Methode übersichtlich gehalten.

Die Umsetzung erfolgte am 14.04.2021 von 14:00 bis 17:00.

Das Ergebnis ist ein steuerbarer Charakter, welcher durch ein leeres Dungeon-Level
gesteuert werden kann. Dabei wird passend die Blickrichtung und die Art der Animation
gewechselt.

# Postmortem

<!--
Bitte blicken Sie auf die Aufgabe, Ihren Lösungsansatz und die Umsetzung
kritisch zurück:
-   Was hat funktioniert, was nicht? Würden Sie noch einmal so vorgehen?
-   Welche Probleme sind bei der Umsetzung Ihres Lösungsansatzes aufgetreten?
-   Wie haben Sie die Probleme letztlich gelöst?
-->

Beim Testen der oben beschriebenen Umsetzung fiel auf, dass der Spielcharakter
sich diagonal schneller bewegt als rein horizontal oder vertikal.
Die Länge des Differenzvektors zwischen `this.position` und `newPosition`
ist im diagonalen Fall um den Faktor $\sqrt{2}$ größer, da `movementSpeed` sowohl
auf die x- als auch auf die y-Komponente addiert wird.

Um ein einheitlicheres Spielerlebnis zu bieten, muss daher der Differenzvektor auf die
Länge von `movementSpeed` normiert werden.
Dies geschieht nach der Formel $\mathbf{n} = \frac{\mathbf{d}}{||\mathbf{d}||} * m$, wobei
$\mathbf{n}$ der normierte Vektor, und $\mathbf{d}$ der Differenzvektor aus `this.position`
und `newPosition` und $m$ `movementSpeed` sind. Anschließend wird der normierte
Differenzvektor komponentenweise zu der aktuellen Position hinzuaddiert, um die
neue Position zu berechnen.
