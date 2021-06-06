---
title:  'Lerntagebuch zur Bearbeitung von Blatt 04' author:

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

# Feature #

## Roboterbegleiter ##

Als neues Feature soll ein Roboterbegleiter für den Helden hinzugefügt werden,
welcher durch den Spieler selbst programmiert wird. Hierfür soll eine einfache
Skriptsprache definiert werden, welche eine vereinfachte API bereitstellt, um
auf die Spielwelt zuzugreifen. Der Spieler soll in der Lage sein, über einen
rudimentären Ingame-Texteditor ein einfaches Verhaltensprogramm für den Roboter
zu schreiben und zu speichern. Das erstellte Programm soll über die Dauer eines
Spieldurchlaufs erhalten bleiben.

Als API sollen einige Methoden zur Verfügung stehen, mithilfe derer Informationen über
die Spielwelt abgefragt werden können (bspw. die Position des Helden) und die
Spielwelt manipuliert werden kann (Hebe das Item auf der aktuellen Position auf
und füge es zum Inventar des Helden hinzu). Durch die so erstellten Programme
soll der Spieler in der Lage sein, die Funktion des Roboterbegleiters selbst zu
definieren (Helden heilen, das Level nach Items durchsuchen, im Kampf helfen).

# Technik #

Für die Realisierung des Roboterbegleiters lassen sich drei Teilaspekte
definieren.

## Ingame-Texteditor ##

Der Spieler muss in der Lage sein, innerhalb des Spiels ein Programm zu
schreiben. Hierfür soll mithilfe von Swing ein rudimentärer Texteditor
implementiert werden, welcher bspw. das bereitgestellte Textfeld nutzt. Über
einige Buttons kann das Programm gespeichert oder ein bereits gespeichertes
Programm geladen werden.

## Parser, Interpreter, formale Grammatik ##

Für die Skriptsprache muss eine formale Grammatik definiert werden. Das
geschriebene Programm muss anschließend geparst und interpretiert werden. Der
Parser (bzw. Lexer) soll dabei RegExp-basiert sein, sodass Skriptprogramme
relativ einfach in Tokens unterteilt werden können. Als Parser soll ein
Shift-Reduce Parser verwendet werden, da innerhalb des Teams bereits Erfahrung
bei der Implementierung eines solchen Parsers vorliegt. Die formale Grammatik
soll relativ einfach gehalten werden (Variablendeklaration, Verzweigungen,
Schleifen, intrinsische Funktionen, einfache arithmetische Operationen auf
Variablen).

Variablen sollen in einer Hashmap (Name als Schlüssel, Object als Wert)
verwaltet werden. Die Typprüfung soll dabei durch die Javaruntime übernommen
werden, sodass Typfehler erst während der Laufzeit auftreten. Jedoch wird
hierdurch Komplexität eingespart.

Zur Interpretation des Programms (also das Ausführen der Operationen, welche mit einem Token
verknüpft sind) kann das Visitor-Pattern verwendet werden.

## Standardbibliothek ##

Der Skriptsprache muss eine gewisse "Standardbibliothek" an Funktionen zur
Verfügung stehen, über welche Informationen über die Spielwelt abgefragt werden
können. Dies umfasst bspw. die aktuelle Position des Helden oder die Position des
räumlich nächsten Gegners. Des Weiteren muss auch eine gewisse Manipulation der
Spielwelt möglich sein, wie z.B. die Navigation zu einer bestimmten Position
oder das Angreifen eines Gegners.

Die zur Realisierung dieser Funktionen nötige Logik muss abstrahiert werden und
dem Spieler eine Art Sandbox bieten, mit der über eine vereinfachte API auf die
Spielwelt zugegriffen werden kann.


# Mögliche Probleme #

## GUI ##

Der Spieler soll in der Lage sein, Programme zu speichern und so über die Dauer
eines Spieldurchlaufs hinaus verwenden können. In welcher Form genau dieses
Speichern und Laden ablaufen soll, ist noch nicht geklärt und ist mit einiger
Komplexität verbunden.

Des Weiteren soll immer nur eine Instanz des Ingame-Texteditors geöffnet werden
können. Dies setzt ein gewisses Maß an Synchronisierung voraus.

## Parser, Interpreter, formale Grammatik ##

Insbesondere die Verwendung der Standardbibliotheksfunktionen mit den korrekten Parametern
könnte kompliziert werden, da keine statische Typprüfung durch den Interpreter
selbst stattfindet. Des weiteren stellt auch die Generierung von
aussagekräftigen Fehlermeldungen eine Herausforderung dar. Die Schwere der Nichterfüllung
von dieser Herausforderung könnte dadurch abgeschwächt werden, dass der Spieler
nur sehr einfache Programme erstellen darf, welche bspw. einem Zeichenlimit
unterliegen, sodass die Programme relativ übersichtlich bleiben und Fehler noch
einfach nachvollzogen werden können.

## Standardbibliothek ##

Die Funktionen der Standardbibliothek sollten im Idealfall nur aus bereits
bestehenden Methoden zusammengesetzt werden und möglichst wenig Änderungen an
der bestehenden Codebasis voraussetzen. Inwiefern dies tatsächlich erreichbar ist, ist noch
nicht 100%ig absehbar.

