from os import pardir
from os import path
from shutil import make_archive
from shutil import rmtree
from subprocess import call as subprocess_call
from sys import argv
from xml.etree import ElementTree


def maven_install():
    distributions = ','.join(argv[1:])
    print(f"Creating distributions for [{distributions}]")
    subprocess_call(['mvn', 'install', '-P', f'"{distributions}"'])

def archive():
    print("Creating distribution archives")
    tree = ElementTree.parse('pom.xml')
    root = tree.getroot()
    client_name = f'Eriantys-{root.find("{http://maven.apache.org/POM/4.0.0}version").text}'
    client_path = path.join(pardir, 'executables', client_name)
    make_archive(client_path, 'zip', path.join(pardir, 'executables'), client_name)
    rmtree(client_path)

def main():
    maven_install()
    archive()


if __name__ == '__main__':
    main()
