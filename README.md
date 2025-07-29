# JSON v HTML Pretvornik

Java aplikacija, ki pretvori JSON datoteke v HTML strani.

## Namestitev

1. Prenesite datoteke
2. Kompajlirajte:
```bash
javac -cp "src/json-20070829.jar" src/*.java -d .
```

## Uporaba

```bash
# Osnovna uporaba (input.json → output.html)
java -cp ".;src/json-20070829.jar" Main

#Z uporabo argumentov
java -cp ".;src/json-20070829.jar" Main input.json izkodek.html
```

## JSON Struktura

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

