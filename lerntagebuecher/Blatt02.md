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

tbd

Ein Teil der Aufgabe ist die Implementierung eines simplen Kampfsystems, welches
automatisch einsetzt, sobald sich ein Monster und der Held auf dem gleichen Feld
befinden. Der Kampf zwischen dem Monster und dem Helden soll parametrierbar sein,
sodass z.B. abhängig vom Gegner eine andere Trefferchance berechnnet wird.
Bei einem Treffer durch einen Gegner soll der Held ein Stück zurückgeschleudert
werden.

# Ansatz und Modellierung

<!--
Bitte hier den Lösungsansatz kurz beschreiben:
-   Wie sollte die Aufgabe gelöst werden?
-   Welche Techniken wollten Sie einsetzen?
-   Wie sah Ihre Modellierung aus (UML-Diagramm)?
-   Worauf müssen Sie konkret achten?
-->

tbd

## Kampfsystem ##

Jedes Monster und der Held müssen das Kampfsystem unterstützen. Um die
Codeduplikation minimal zu halten, wird daher ein gemeinsames Interface
`ICombatable` definiert, welches die Basisfunktion des Kampfsystems beinhaltet.
Im folgenden ist das UML-Diagramm des `ICombatable`-Interfaces dargestellt:
![ICombatable](./Blatt02/ICombatable.png "ICombatable interface")


Die Kernmethode `attackTargetIfInRange` soll in der update-Methode der
implementierenden Klasse aufgerufen werden und automatisch nach potentiell
angreifbaren `ICombatable`-Instanzen nahe der eigenen Position suchen.

Findet diese Methode ein angreifbares Ziel, wird dieses mit `setTarget` gecached, um in nachfolgenden
`update`-Aufrufen nicht erneut nach Zielen zu suchen. Falls sich das Ziel aus
dem Angriffsbereich hinausbewegt, wird der Ziel-Cache zurückgesetzt. Standardmäßig ist
der Angriffsbereich das aktuelle Feld, auf dem die `ICombatable`-Instanz steht.

In `attack` wird zusammen mit der eigenen `hitChance` und er
`evasionChance` der angegriffenen `ICombatable`-Instanz die Erfolgschance eines
Angriffs berechnet. Bei einem erfolgreichen Angriff wird der angegriffenen
`ICombatable`-Instanz mit `dealDamage` Gesundheit abgezogen. Falls die
Gesundheit eines `ICombatable` auf Null sinkt, stirbt es (dies kann über die
Methode `isDead` abgefragt werden).

# Umsetzung

<!--
Bitte hier die Umsetzung der Lösung kurz beschreiben:
-   Was haben Sie gemacht,
-   an welchem Datum haben sie es gemacht,
-   wie lange hat es gedauert,
-   was war das Ergebnis?
-->

tbd

## Kampfsystem ##

Der prinzipielle Ablauf eines Kampfes ist bereits unter dem Kapitel _Ansatz und
Modellierung_ beschrieben. Daher wird hier nur noch auf die hervorzuhebenden
Implementierungsdetails eingegangen.

### Zielfindung ###

Zur Identifikation eines potentiellen Ziels für einen Angriff
wird zunächst der naive Ansatz gewählt, über alle `IEntity`-Instanzen im Spiel zu iterieren und die
erste Instanz zurückzugeben, welche ebenfalls `ICombatable` implementiert.
Dies kann mit dem `instanceof`-Keyword überprüft werden. Implementiert die
`ICombatable`-Instanz zusätzlich das `IDrawable`-Interface, wird
über dieses Interface die aktuelle Position des potentiellen Ziels ausgelesen.

Mit der `getTileAt`-Methode des `DungeonLevel`s wird ermittelt, ob das
potentielle Ziel auf dem gleichen Feld steht und somit angreifbar ist.
Dieser Ansatz setzt voraus, dass die `ICombatable`-Instanz eine Referenz auf die
`Game`-Instanz hält (um Zugriff auf die `IEntity`-Instanzen des
`EntityControllers` zu haben). Daher muss die `Actor`-Klasse eine Referenz auf
das `Game`-Objekt halten, welches um die Methode `getAllEntities` erweitert
wird.

### Angriffsfrequenz ###

Da `attackTargetIfInRange` in jedem Durchlauf der
`update`-Methode aufgerufen wird, muss eine maximale Frequenz festgelegt werden,
in der die `ICombatable`-Instanz angreifen kann. Hierzu wird innerhalb der
default-Implementierung von `attackTargetIfInRange` die Methode `canAttack`
aufgerufen, welche von der konkreten `ICombatable`-Instanz implementiert werden
muss. Die `Actor`-Klasse gibt hier eine interne boolesche Variable zurück,
welche nach einem Angriff `false` ist und erst nach der Zeit `attackDelay`
mithilfe eines `Timer`s wieder auf `true` gesetzt wird.

### Zurückschleudern ###

Der Spielcharakter soll eine gewisse Distanz zurückgeschleudert werden, falls er
Schaden erleidet. Die Implementierung hierfür wird in der `Actor`-Klasse vorgenommen,
sodass auch Monster theoretisch zurückgestoßen werden können (evtl. durch eine
Fähigkeit des Helden).
In der `dealDamage`-Implementierung wird durch die Position des Angreifers und
einer definierten `knockBackDistance` der Zielpunkt des Rückstoßes ermittelt.
Da der Spielcharakter nicht einfach innerhalb eines Frames an diese Position teleportiert
werden soll, wird die `update`-Methode des `Actor`s erweitert.
Hier wird zwischen zwei `MotionStates`
unterschieden (CAN_MOVE und IS_KNOCKED_BACK). Ist der aktuelle `MotionState`
`CAN_MOVE`, kann der Spielcharakter wie gewohnt bewegt werden. Im Fall von
`IS_KNOCKED_BACK` berechnet `calculateKnockBackTarget` mit `knockBackSpeed` die
neue Position für den aktuellen Frame. Hierzu wird der Differenzvektor der
aktuellen Position und des Zielpunktes auf `knockBackSpeed` skaliert und zur
aktuellen Position addiert.
Falls die neue Position nicht erreichbar ist (durch `getTileAt` abgefragt) oder
der Zielpunkt für das Zurückstoßen
erreicht wurde, wird der `MotionState` zurück auf `CAN_MOVE`-gesetzt.

20.04.2021: 17:00 - 20:00: Basisentwurf Kampfsystem gemeinsam erarbeiten.
21.04.2021: 17:00 - 20:00: Kampfsystem ausimplementieren und testen.
24.04.2021: 12:00 - 15:00: Rückstoß implementieren und testen.

# Postmortem

<!--
Bitte blicken Sie auf die Aufgabe, Ihren Lösungsansatz und die Umsetzung
kritisch zurück:
-   Was hat funktioniert, was nicht? Würden Sie noch einmal so vorgehen?
-   Welche Probleme sind bei der Umsetzung Ihres Lösungsansatzes aufgetreten?
-   Wie haben Sie die Probleme letztlich gelöst?
-->

tbd

## Kampfsystem ##

Der naive Ansatz, zur Erkennung von angreifbaren `IEntity`-Instanzen über alle
im Level befindlichen `IEntity`-Instanzen zu iterieren hat noch
Optimierungsbedarf. Dies ist insbesondere der Fall, da jede
`ICombatable`-Instanz diese Iteration durchführt. Daher darf der aktuelle
Zustand des Kampfsystems nur als erster Startpunkt betrachtet werden. Eine
effizientere Lösung wäre beispielsweise eine
[spatial hashmap](https://www.gamedev.net/articles/programming/general-and-gameplay-programming/spatial-hashing-r2697/).
mit der gehashten Koordinaten eines Feldes als Schlüsselwert und den `IEntity`-Instanzen auf
diesem Feld als Wert. So müsste nur am Anfang jedes Frames einmal über alle
`IEntity`-Instanzen iteriert werden, um die Hashmap mit den aktuellen Position
der Instanzen zu aktualisieren. Mit solche einer Hashmap könnte eine
`ICombatable`-Instanz genau die Felder nach potentiellen Zielen durchsuchen, die
auch in dem möglichen Angriffsradius liegen. Aus zeitlichen Gründen wird diese
Umsetzung allerdings auf einen späteren Zeitpunkt verschoben.

Bei der Umsetzung des Zurückschleuderns des Helden kam es häufig zu dem Problem,
dass die Überprüfung, ob der Zeilpunkt bereits erreicht ist, nicht den passenden
Wert geliefert hat und der Spielcharakter anschließend um den Zielpunkt
oszillierte. Dies ist darauf zurückzuführen, dass die berechnete Position
etwas zu weit hinter dem Zielpunkt liegt, falls die Distanz zwischen aktueller
Position und Zielpunkt kleiner als `knockBackSpeed` ist. Im folgenden Frame
liegt dann entsprechend die neue berechnete Position wieder etwas zu weit vor
dem Zielpunkt.
Daher wurde eine zusätzliche Überprüfung dieses Falls hinzugefügt. Kommt es zu diesem Fall, wird
die neue Position auf den Zielpunkt gesetzt und `MotionState` auf `CAN_MOVE`
gesetzt, um das Zurückschleudern zu beenden.
