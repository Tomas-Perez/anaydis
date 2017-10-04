import os.path as path
import re
import csv

SRC_DIR = path.abspath(path.join(__file__, "../../../../.."))
BOOKS_PATH = "".join([SRC_DIR, '/test/resources/books'])
QUIXOTE_PATH = "".join([BOOKS_PATH, '/quijote.txt'])
QUIXOTE_PARSE_PATH = "".join([BOOKS_PATH, '/quijote_parsed.txt'])

regex = re.compile(r"\b[a-zA-Z]+\b")

with open(QUIXOTE_PATH, encoding='utf-8') as quixote:
    with open(QUIXOTE_PARSE_PATH, 'w', encoding='utf-8', newline="") as parsed:
        writer = csv.writer(parsed, delimiter=',')
        for line in quixote:
            matches = [word.lower() for word in regex.findall(line)]
            if len(matches) > 0:
                writer.writerow(matches)