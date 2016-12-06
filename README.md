led-lenkrad
====

LED-Lenkrad zur Visualisierung der Drehzahl

Die Arbeit zeigt den Weg der Ideenfindung, dem Design und die Implementierung des Prototypen und die finalen Auswertung der Studienfrage, ob diese Interaktionsmodalität von Benutzern favorisiert wird.

Für dieses Projekt wurde ein 
Dieser besteht aus einer Sitzgelegenheit
mit zwei Pedalen (Gas und Bremse), samt einem Fanatec Porsche GT3
Lenkrad. An diesem Lenkrad montierten wir am Aueren Rand einen LEDStreifen
in einem transparenten Plastikschlauch und verbanden ihn uber ein
Arduino-Sensor-Board mit dem Fahrsimulator-PC. Auf diesem PC lauft der
OpenSource Fahrsimulator OpenDS2, basierend auf der Programmiersprache
Java.
Mittels RMI (Remote Method Invocation) konnten wir dann Methoden aus
OpenDS abrufen und die ruckgegebenen Daten am LED-Streifen anzeigen.
Diese Seminararbeit beschrankt sich darauf, die Drehzahl mittels der LED's
darzustellen.
Hierfur wurden drei Methoden entwickelt, die Drehzahl zu Visualisieren.
