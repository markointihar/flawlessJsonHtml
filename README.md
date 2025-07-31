# JSON v HTML Pretvornik

Java aplikacija, ki pretvori JSON datoteke v HTML strani.

## Namestitev

1. Prenesite datoteke
2. Prenos knjižnic:
```bash
mvn clean compile
```

# Uporaba
Toplo priporočam zagon znotraj IntelliJ, če ni mogoče:
```bash
# Osnovna uporaba (input.json → output.html)
mvn exec:java

#Z uporabo argumentov
mvn exec:java -Dexec.args="input.json output.html"
```
## Ustvarjanje JAR datoteke
```bash
mvn clean package
```
## Zagon JAR datoteke
```bash
java -jar target/json-html-converter-1.0.0-jar-with-dependencies.jar input.json output.html
```

# JSON Struktura

```json
{
  "doctype": "html",
  "language": "sl",
  "head": {
    "meta": {
      "charset": "utf-8",
      "viewport": {
        "width": "device-width",
        "initial-scale": "1"
      }
    },
    "title": "Naslov Strani"
  },
  "body": {
    "h1": "Glavni Naslov",
    "p": "Vsebina strani...",
    "div": {
      "attributes": {
        "class": "kontejner",
        "id": "glavna-vsebina"
      },
      "h2": "Podnaslov"
    }
  }
}
```

## Izhod

```html
<!DOCTYPE html>
<html lang="sl">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Naslov Strani</title>
	</head>
	<body>
		<h1>Glavni Naslov</h1>
		<p>Vsebina strani...</p>
		<div class="kontejner" id="glavna-vsebina">
			<h2>Podnaslov</h2>
		</div>
	</body>
</html>

```

