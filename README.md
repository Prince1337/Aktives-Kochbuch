# Aktives Kochbuch
## Architecture Kata „Aktives Kochbuch“
Entwickle eine (Web-)Software, die ihrem Anwender Vorschläge macht, was er kochen könnte. Damit soll dem Anwender geholfen werden, sich gut zu ernähren durch eigenes Kochen, ohne immer wieder vor der Frage zu stehen „Was soll ich denn kochen?“

Der Anwender stellt zunächst mit der Anwendung sein persönliches Kochbuch zusammen. Jedes Rezept besteht aus einem Rezepttitel und der Rezeptur (d.h. einem längeren Text). Zusätzlich können jedem Rezept noch Tags zugeordnet werden.

Die Hauptfunktion der Software besteht darin, dem Anwender aus der wachsenden Sammlung von Rezepten in seinem Kochbuch Vorschläge zu machen. Wenn der Anwender um Vorschläge bittet, dann gibt er an, wie viele er haben möchte (1,2,3…) und aus welcher mit Tags definierten Untermenge an Rezepten sie ausgewählt werden sollen.

Der Anwender kann entweder interaktiv einmalig Vorschläge anfordern. Oder er stellt einen Rhythmus ein, in dem Vorschläge geschickt werden; das Kochbuch wird dann automatisch aktiv.

Der Rhythmus wird durch Angabe der Wochentage beschrieben, an denen Vorschläge geschickt werden, z.B. „jeden Freitag und Samstag“. Außerdem kann zu jedem Tag eingestellt werden, um welche Uhrzeit die Vorschläge eintreffen sollen.

Die Vorschläge treffen beim Anwender per Email ein, die Titel und Bild enthält und einen Link auf das vollständige Rezept im Kochbuch.