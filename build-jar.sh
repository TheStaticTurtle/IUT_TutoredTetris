if [ -d "out" ]
then
	[ -e Tetris.jar ] && rm Tetris.jar
	cd out
	jar -f ../Tetris.jar -e fr.iut.tetris.Main -v -c ../tetris/libs/discord-rpc.jar *
else
	echo "Classes directories doesn't exist. Building sources"
	./build.sh
	./build-jar.sh
fi
