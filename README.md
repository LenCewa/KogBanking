# KogBanking
IBM Hackatron
 - Hier werden kurz die Appanwendungen erklärt
 - Diese gilt insbesondere für Personen mit Sehschwäche
 - Universelle Bankingapp (Institutunabhängig)

## Aufbau der App

### Login
Es soll ein Login ermöglicht werden mit:
- Gesichtserkennung
- Fingerabdruckscanner
- **Beim ersten Login** sollen die Kontodaten abgefragt werden

### Keine TAN
- Durch die Spracherkennung muss der Nutzer keine TAN eingeben sofern Watson die Stimme
  als richtig verifiziert
- Ist sich Watson nicht sicher wird eine zusätzliche Gesichtserkennung eingefordert

### Speichern der ein- und ausgehenden Überweisungen
Jede Überweisung die ein- oder ausgeht wird auf einer Cloud gespeichert. So können die Daten sofort abgefragt werden über

1. IBAN
2. Name
3. Kreditinstitut
um einen schnelleren Überweisungsvorgang zu ermöglichen.
  
### Notifications
Es gibt mehrere Möglichkeiten Notifications zu erhalten

Typ | Ziel | Einstellung
--- | --- | ---
Deadlines für eingehende Überweisungen | Man kann der App einen Namen (der in der Datenbank hinterlegt ist) und diese Prüuft ob der einzureichende Betrag in der richtigen Höhe zum Richtigen Zeitpunkt eingegangen ist. So muss man seiner Überweisung nicht hinterherlaufen. | Es kann eingestellt werden wann und wie man die Benachrichtiung erhalten will
Wochenreport | Überblick über die entstandenen entstandenen Transaktionen der Woche | Zeitraum einstelen, etc.
Warnung | Kenntlich machen wenn unstimmigkeiten sichtbar sind | Dringlichkeit definieren, etc.
Reminder | Falls man eine Überweisung tätigen will, kann man hier die Überweisung im Entwurf speichern und die App benachritigt den Nutzer wenn der Zeitpunkt der Überweisung stattgefunden hat. Diese kann durch eine einfache Bestätigung durchgeführt werden. Familie könnte vorgefertigte Überweisungen definieren (bspw.) | `TODO`

### Datenspeicherung
- Die IBAN kann Verbal per *voice to text* gespeichert werden
- Die IBAN kann durch das *visuelle einlesen der Karte* eingespeichert werden

### Überweisungsvorgang
1. Ich möchte eine Überweisung tätigen
2. Namen Nennen -|- Name nicht bekannt? -> Daten das erste mal Speichern (weiter mit 3.)
3. Name ist bekannt
4. Daten werden automatisch aufgefüllt
5. Betragshöhe angeben
6. Bestätigung des Inputs
7. TAN? [siehe hier](#keine-tan)
8. Wiederholung der Überweisung
9. Durchführen der Überweisung

### Wunschfeature
- NFC Handy an Handy legen und eine Fast-Forward Überweisung
