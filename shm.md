Spatial Hash Map:
Problem: Aktuell keine Effiziente Loesung, um potentielle Targets fuer Angriff
oder Item Aufheben, etc. in bestimmtem Radius zu ermitteln

Folge: Items, Chests, ICombatable-Objekte iterieren ueber alle Entities des
EntityControllers und testen auf Intersection. Ineffizient!

Loesung:
HashMap mit Koordinaten als Key und Entity / Actor / DrawableEntity / irgendwas
mit Position halt als Value.

Wichtige Aspekte:
- Benoetigte Bucketanzahl -> Abhaengig von Anzahl der Entities!
- Art der Hashfunktion? -> Zwei Koordinaten eindeutig hashen -> Welche Range
  koennen Koordinaten annehmen? nur positive Zahlen, Range aktuell bis max. 50
  	- Koord runden oder flooren? Runden: verschiebung von Zellen um Haelfte
	  einer Tile nach rechts oben, daher Flooren!
	- potentielle Hashfunktion: x-Koord auf int runden und 1 byte nach links
	  verschieben ( * 256), y-Koord nur auf int runden, mod Bucketsize.
	- (x << 8 + y) mod bucketCount
- Sondierung oder Linked list? -> Beides. Quadratische Sondierung fuer Key
  (Position) Kollision und Linked List and Position fuer die Entities, die die
  gleiche Position haben (laut Zelleneinteilung)
- Festlegung des Bucketcounts zu Primzahl

- Einzelne Entities koennen nicht eindeutig anhand ihres Keys ermittelt werden,
  gefloort wird. Außerdem ändert sich die Position der Entities sodass ein
  remove(Position) keinen wirklichen Sinn ergibt
