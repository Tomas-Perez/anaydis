import os.path as path
import re
import csv

SRC_DIR = path.abspath(path.join(__file__, "../../../../.."))
BOOKS_PATH = "".join([SRC_DIR, '/test/resources/books'])
QUIXOTE_PATH = "".join([BOOKS_PATH, '/quijote.txt'])
QUIXOTE_PARSE_PATH = "".join([BOOKS_PATH, '/quijote_parsed.txt'])
QUIXOTE_REVERSE_PATH = "".join([BOOKS_PATH, '/quijote_reversed.txt'])


def parse_english_to_csv(in_file, out_file):
    regex = re.compile(r"\b[a-zA-Z]+\b")
    with open(in_file, encoding='utf-8') as quixote:
        with open(out_file, 'w', encoding='utf-8', newline="") as parsed:
            writer = csv.writer(parsed, delimiter=',')
            for line in quixote:
                matches = [word.lower() for word in regex.findall(line)]
                if len(matches) > 0:
                    writer.writerow(matches)


def reverse_file(in_file, out_file):
    with open(in_file, encoding='utf-8') as parsed:
        with open(out_file, 'w', encoding='utf-8', newline="") as reversed:
            for line in parsed:
                reversed.write(line[::-1])

if __name__ == '__main__':
    reverse_file(QUIXOTE_PARSE_PATH, QUIXOTE_REVERSE_PATH)
