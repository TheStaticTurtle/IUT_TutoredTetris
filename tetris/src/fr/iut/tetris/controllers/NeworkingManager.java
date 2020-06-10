package fr.iut.tetris.controllers;

import fr.iut.tetris.Log;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.BlockModel;
import fr.iut.tetris.models.PieceModel;
import sun.nio.ch.Net;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NeworkingManager {
	String host;
	int port;
	Socket socket;
	OutputStreamWriter writer;
	InputStreamReader reader;
	CoopController ctrl;

	boolean server = true;

	public NeworkingManager(String host,int port, CoopController ctrl) {
		this.host = host;
		this.port = port;
		this.ctrl = ctrl;
		try {
			socket = new Socket(this.host, this.port);
			this.writer = new OutputStreamWriter(socket.getOutputStream());
			this.reader = new InputStreamReader(socket.getInputStream());

			Thread bgThread = new Thread(new ServerListner(this.writer,this.reader,this, ctrl));
			bgThread.start();

		} catch (IOException e) {
			Log.error(this,"Failed to connect to server: "+e.getMessage());
		}
	}

	void sendData(String data) {
		try {
			writer.write(data);
			writer.flush();
		} catch (IOException e) { e.printStackTrace(); }
	}
}

class ServerListner implements Runnable {
	protected OutputStreamWriter writer;
	protected InputStreamReader reader;
	NeworkingManager manager;
	CoopController coopController;

	public ServerListner( OutputStreamWriter writer,  InputStreamReader reader, NeworkingManager manager , CoopController coopController ) throws IOException {
		this.writer = writer;
		this.reader = reader;
		this.manager = manager;
		this.coopController = coopController;
	}

	public void amIAlone() throws IOException {
		long aloneTimeout = System.currentTimeMillis() + 1000;
		writer.write("Hello?\n");
		writer.flush();
		while(System.currentTimeMillis() < aloneTimeout) {
			if(reader.ready()) {
				Log.info(this,"Hey I'm not alone");
				this.manager.server = false;
				break;
			}
		}
		if(System.currentTimeMillis() >= aloneTimeout) {
			Log.info(this,"Hello darkness my old friend");
			this.manager.server = true;
		}
	}

	public void run() {
		try {
			Thread.sleep(250);
			amIAlone();
		} catch (IOException | InterruptedException ignored) {}

		while (!Thread.currentThread().isInterrupted()) {
			try {
				if(this.reader.ready()) {
					StringBuilder data = new StringBuilder();
					while(!data.toString().endsWith("\n")) {
						data.append((char) reader.read());
					}

					if(data.toString().equals("Hello?\n")) { //Client-Server find
						writer.write("Hi\n");
						writer.flush();
					}

					/*
							FROM PLAYER A TO B
					 */
					else if(data.toString().startsWith("BEGIN") && !this.manager.server) {
						this.coopController.model.gameState = GameState.PLAYING;
						continue;
					}
					else if(data.toString().startsWith("END") && !this.manager.server) {
						this.coopController.model.gameState = GameState.FINISHED;
						continue;
					}
					else if(data.toString().startsWith("BOARD") && !this.manager.server) {
						this.coopController.model.gameState = GameState.PLAYING;
						String raw = data.toString().split("-")[1];
						String boardData = raw.substring(0,raw.length()-3);
						this.manager.ctrl.model.pieceList.clear();
						int y=0;
						int x=0;
						for (String line: boardData.split("__")) {
							for (String block: line.split("_")) {
								if(!block.equals("null")) {
									BlockModel m = new BlockModel(Color.decode(block));
									m.standAlonePos = new Point(x,y);
									this.manager.ctrl.model.pieceList.add(m);
								}
								x+=1;
							}
							y+=1;
							x=0;
						}
						this.manager.ctrl.vue.recalculate();
						continue;
					}
					else if(data.toString().startsWith("NPIECEA-") && !this.manager.server) {
						this.coopController.model.gameState = GameState.PLAYING;
						String raw = data.toString().split("-")[1];
						String boardData = raw.substring(0,raw.length()-3);

						BlockModel[][] ch = new BlockModel[4][4];
						int y=0;
						int x=0;
						for (String line: boardData.split("__")) {
							for (String block: line.split("_")) {
								if(!block.equals("null")) {
									ch[y][x] = new BlockModel(Color.decode(block));
								} else {
									ch[y][x] = null;
								}
								x+=1;
							}
							y+=1;
							x=0;
						}
						this.manager.ctrl.model.nextPiecePlayerA = new PieceModel(ch, new Point(0,0), new Point(0,0),"nn");
						continue;
					}
					else if(data.toString().startsWith("NPIECEB-") && !this.manager.server) {
						this.coopController.model.gameState = GameState.PLAYING;
						String raw = data.toString().split("-")[1];
						String boardData = raw.substring(0,raw.length()-3);

						BlockModel[][] ch = new BlockModel[4][4];
						int y=0;
						int x=0;
						for (String line: boardData.split("__")) {
							for (String block: line.split("_")) {
								if(!block.equals("null")) {
									ch[y][x] = new BlockModel(Color.decode(block));
								} else {
									ch[y][x] = null;
								}
								x+=1;
							}
							y+=1;
							x=0;
						}
						this.manager.ctrl.model.nextPiecePlayerB = new PieceModel(ch, new Point(0,0), new Point(0,0),"nn");
						continue;
					}

					/*
							FROM PLAYER B TO A
					 */
					else if(data.toString().startsWith("DOWN") && this.manager.server) {
						this.coopController.model.fallCurrentForPlayerB();
						continue;
					}

					else if(data.toString().startsWith("FDOWN") && this.manager.server) {
						this.coopController.model.fallCurrentAtBottomForPlayerB();
						continue;
					}

					else if(data.toString().startsWith("MOVEX,") && this.manager.server) { //Move piece position
						int m = Integer.parseInt(data.toString().split(",")[1].split("\n")[0]);
						if(m < 0) this.coopController.model.moveCurrentX(1,Direction.LEFT);
						if(m > 0) this.coopController.model.moveCurrentX(1,Direction.RIGHT);
						continue;
					}
					else if(data.toString().startsWith("ROTATE,") && this.manager.server) { //Move piece position
						int m = Integer.parseInt(data.toString().split(",")[1].split("\n")[0]);
						if(m < 0) this.coopController.model.rotateCurrent(1,Direction.LEFT);
						if(m > 0) this.coopController.model.rotateCurrent(1,Direction.RIGHT);
						continue;
					}

					else {
						Log.critical(this,data.toString());
					}
				}

				if(this.manager.server && this.manager.ctrl.model.gameState == GameState.PLAYING) {
					try {
						this.writer.write("BOARD-");
						for (BlockModel[] line: this.coopController.model.computeMixedGrid()) {
							for (BlockModel block: line) {
								if(block != null && block.color != null) {
									this.writer.write("#"+Integer.toHexString(block.color.getRGB()).substring(2)+"_");
								} else {
									this.writer.write("null_");
								}
							}
							this.writer.write("_");
						}
						this.writer.write("\n");
						this.writer.flush();

						this.writer.write("NPIECEA-");
						for (BlockModel[] line: this.coopController.model.nextPiecePlayerA.childs) {
							for (BlockModel block: line) {
								if(block != null && block.color != null) {
									this.writer.write("#"+Integer.toHexString(block.color.getRGB()).substring(2)+"_");
								} else {
									this.writer.write("null_");
								}
							}
							this.writer.write("_");
						}
						this.writer.write("\n");
						this.writer.flush();

						this.writer.write("NPIECEB-");
						for (BlockModel[] line: this.coopController.model.nextPiecePlayerB.childs) {
							for (BlockModel block: line) {
								if(block != null && block.color != null) {
									this.writer.write("#"+Integer.toHexString(block.color.getRGB()).substring(2)+"_");
								} else {
									this.writer.write("null_");
								}
							}
							this.writer.write("_");
						}
						this.writer.write("\n");
						this.writer.flush();
					} catch (PieceOutOfBoardException | OverlappedPieceException e) {
						e.printStackTrace();
					}
				}

				try {
					Thread.sleep(this.manager.ctrl.model.fallSpeed - 50);
				} catch (InterruptedException ignored) {}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}