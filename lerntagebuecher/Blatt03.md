---
title:  'Lerntagebuch zur Bearbeitung von Blatt 03' author:

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
Der erste Teil der Aufgabe ist das Implementieren von einsammelbaren Gegenständen, wie Tränke, Waffen oder
Zaubersprüche. Die Items sollen im OOP-Gedanken erstellt werden. Der zweite Teil der Aufgabe ist die Implementierung
eines Inventars in dem aufgesammelte Gegenstände mitgeführt werden sollen. Aus dem Inventar sollen Gegenstände
ausrüstbar sein sowie Item wie Tränke aufgebraucht werden können. Im dritten Teil der Aufgabe sollen Schatztruhen mit
zufälligem Loot erstellt werden sowie Taschen, die mehrere Items halten können.

# Ansatz und Modellierung

<!--
Bitte hier den Lösungsansatz kurz beschreiben:
-   Wie sollte die Aufgabe gelöst werden?
-   Welche Techniken wollten Sie einsetzen?
-   Wie sah Ihre Modellierung aus (UML-Diagramm)?
-   Worauf müssen Sie konkret achten?
-->

## Items ##

- ItemFactory zum Erstellen der Items
- Item Basisklasse für das Inventar
- Für jeden Itemtyp eine Basisklasse (Weapon, Shield, Scroll, Potion)

## Inventar ##

- Wrapper ArrayList von Items
- Menü zum durchgehen als Statemachine
-

## Schätze und Taschen ##

- Schatztruhen haben eigenes Inventar
-
-

# Umsetzung

<!--
Bitte hier die Umsetzung der Lösung kurz beschreiben:
-   Was haben Sie gemacht,
-   an welchem Datum haben sie es gemacht,
-   wie lange hat es gedauert,
-   was war das Ergebnis?
-->

## Items ##

- Factory für die Items vereinfacht das Erstellen der Items
- TriggerDetection zum Aufsammeln des Items im Level und löschen aus dem Entity Controller
- Über Visitor-Pattern wird die Benutzung der items geloggt.

## Inventar ##

- Ein Item kann über das Visitor-Pattern vom Hero genutzt werden.

## Schätze und Taschen ##

- Schatztruhen kriegen Referenz zum Öffner

# Postmortem

<!--
Bitte blicken Sie auf die Aufgabe, Ihren Lösungsansatz und die Umsetzung
kritisch zurück:
-   Was hat funktioniert, was nicht? Würden Sie noch einmal so vorgehen?
-   Welche Probleme sind bei der Umsetzung Ihres Lösungsansatzes aufgetreten?
-   Wie haben Sie die Probleme letztlich gelöst?
-->

- Zeitmangel
- Abhängigkeiten der Arbeitspakete erschwerten paralleles Arbeiten


