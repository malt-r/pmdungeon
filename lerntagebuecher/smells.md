Malte:
- Spatial Hashmap für Identifikation der nahen IEntities nutzen (Malte)
- viele verteilte Versionen von Intersection-Berechnung (Malte)
- Stats sind lose Variablensammlungen und haben keine einheitliche
  Schnittstelle, die das Hinzufügen und Entfernen von Modifiern (durch z.B. Schwert) unterstützt (Malte)

Tim:
- Main-HUD ist direkt im Game realisiert (vgl. QuestHud) (Tim)
- TODOs auf Aktualitaet checken und ggf. mit hier aufnehmen oder löschen (Tim)
- Fernkampf (Tim)
- GameOver Handling sollte in Methode ausgelagert werden (in Game) (Tim)

Dennis:
- Drawable Entity als Basisklasse für alles, was im Dungeon geupdated und
  gezeichnet werden muss? (Dennis)
- Schlaue Monster mit Strategy-Pattern (Dennis)

Jeder:
- Actor in Statemachine umbauen (falls Zeit)
- alte Kommentare / auskommentierten Code entfernen (jeder, wo er es sieht)

Bugs:
- AUf Level 3 wird mit Heiltränken nicht die Gesundheit über 200 angehoben ->
  sollte auf maxHealth angehoben werden.
