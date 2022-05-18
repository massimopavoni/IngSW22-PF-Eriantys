from subprocess import call as subprocess_call
from sys import argv


def main():
    print(f"Creating distributions for [argv[1]}")
    subprocess_call(['mvn', 'install', '-P', argv[1]])

if __name__ == '__main__':
    main()
