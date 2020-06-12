rm -rf docs/javadoc
cd tetris/src/
javadoc -private -splitindex -d ../../docs/javadoc/ fr/iut/tetris/models/*.java fr/iut/tetris/controllers/*.java fr/iut/tetris/vues/*.java fr/iut/tetris/*.java
