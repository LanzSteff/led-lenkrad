led-lenkrad
====

LED-Lenkrad zur Visualisierung der Drehzahl

Die Arbeit zeigt den Weg der Ideenfindung, dem Design und die Implementierung des Prototypen und die finalen Auswertung der Studienfrage, ob diese Interaktionsmodalität von Benutzern favorisiert wird.

Für dieses Projekt wurde ein Fahrsimulator, welcher aus einer Sitzgelegenheit mit zwei Pedalen (Gas und Bremse), samt einem Fanatec Porsche GT3 Lenkrad besteht, verwendet. An diesem Lenkrad wurde am äußeren Rand ein LED-Streifen in einem transparenten Plastikschlauch motiert und über ein Arduino-Sensor-Board mit dem Fahrsimulator-PC verbunden. Auf diesem PC läuft der OpenSource Fahrsimulator OpenDS2, basierend auf der Programmiersprache Java.
Mittels RMI (Remote Method Invocation) wurden Methoden aus OpenDS abgerufen und die rückgegebenen Daten am LED-Streifen angezeigen.
Diese Arbeit beschränkt sich darauf, die Drehzahl mittels der LED's darzustellen.
Hierfür wurden drei Methoden entwickelt, die Drehzahl zu Visualisieren.
