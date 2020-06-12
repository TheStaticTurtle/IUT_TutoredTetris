if [ -d "out" ]
then
	echo "Cleaning old build"
	rm -rf out
fi

echo "Building models"
javac -classpath out -sourcepath tetris/src -d out tetris/src/fr/iut/tetris/models/*.java
echo "Building controllers"
javac -classpath out -sourcepath tetris/src -d out tetris/src/fr/iut/tetris/controllers/*.java
echo "Building vues"
javac -classpath out -sourcepath tetris/src -d out tetris/src/fr/iut/tetris/vues/*.java
echo "Building main"
javac -classpath out -sourcepath tetris/src -d out tetris/src/fr/iut/tetris/*.java
echo "Copying ressources"
cp -r tetris/src/res out/
