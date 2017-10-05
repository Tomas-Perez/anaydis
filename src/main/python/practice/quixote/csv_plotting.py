import matplotlib.pyplot as plt
import pandas as pd
from practice.quixote.quixoteParser import SRC_DIR
from collections import namedtuple

BENCH_PATH = "".join([SRC_DIR, '/main/java/anaydis/practice/quijote/QuixoteBenchmark.csv'])
Row = namedtuple('Row', ['name', 'values'])


def plot_csv_benchmark(path):
    data = pd.read_csv(path, header=None)
    rows = map(row_to_tuple, data.iterrows())
    n_list = []
    algorithm_list = []
    for row in rows:
        if row.name == 'N':
            n_list.extend(row.values)
        else:
            algorithm_list.append(row)
    for algorithm in algorithm_list:
        plt.plot(n_list, algorithm.values)
    plt.legend(list(map(lambda x: x.name, algorithm_list)))
    plt.xlabel('N')
    plt.ylabel('Time')
    plt.show()


def row_to_tuple(row):
    return Row(row[1][0], row[1][1:].values.tolist())

if __name__ == '__main__':
    plot_csv_benchmark(BENCH_PATH)
