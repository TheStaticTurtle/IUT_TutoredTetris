if [ -d "out" ]
then
	[ -e TutoredTetris.jar ] && rm TutoredTetris.jar
	cd out
	jar -f ../TutoredTetris.jar -e fr.iut.tetris.Main -v -c *
else
	echo "Classes directories doesn't exist. Building sources"
	./build.sh
	./build-jar.sh
fi
