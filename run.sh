#!/bin/bash
# Compilation et exécution de l'application RaPizz

cd "$(dirname "$0")"

# Créer le répertoire bin
mkdir -p bin

echo "========================================"
echo "Compilation de l'application RaPizz..."
echo "========================================"

javac -d bin -cp "mysql-connector-j-9.7.0.jar" \
  src/ra/pizz/*.java \
  src/ra/pizz/model/*.java \
  src/ra/pizz/dao/*.java \
  src/ra/pizz/ui/*.java \
  src/ra/pizz/util/*.java

if [ $? -ne 0 ]; then
    echo "ERREUR: Compilation échouée!"
    exit 1
fi

echo ""
echo "Compilation réussie!"
echo ""
echo "Choisissez une action:"
echo "1 - Test de connexion"
echo "2 - Lancer l'application"
echo ""
read -p "Votre choix (1 ou 2): " choice

case $choice in
    1)
        echo ""
        echo "========================================"
        echo "Test de connexion..."
        echo "========================================"
        java -cp "bin:mysql-connector-j-9.7.0.jar" ra.pizz.TestConnection
        ;;
    2)
        echo ""
        echo "========================================"
        echo "Lancement de l'application..."
        echo "========================================"
        java -cp "bin:mysql-connector-j-9.7.0.jar" ra.pizz.AppMain
        ;;
    *)
        echo "Choix invalide!"
        exit 1
        ;;
esac
