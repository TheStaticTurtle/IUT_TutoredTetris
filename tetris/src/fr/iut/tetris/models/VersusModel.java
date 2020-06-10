package fr.iut.tetris.models;

import fr.iut.tetris.controllers.CoopController;
import fr.iut.tetris.controllers.VersusController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;

import java.util.ArrayList;
import java.util.Random;

public class VersusModel {

    public int height = 20;
    public int witdh = 10;
    public int fallSpeedPlayerA = 1000; //ms
    public int fallSpeedPlayerB = 1000; //ms
    ArrayList<Object> pieceListPlayerA = new ArrayList<>();
    ArrayList<Object> pieceListPlayerB = new ArrayList<>();
    public GameState gameState = GameState.WAITING;
    public PieceModel fallingPiecePlayerA = null;
    public PieceModel fallingPiecePlayerB = null;
    public PieceModel nextPiecePlayerA;
    public PieceModel nextPiecePlayerB;
    VersusController ctrl;
    public int bestScore = 0;
    public int currentScorePlayerA = 0;
    public int currentScorePlayerB =0;
    Random rand = new Random();

    public void setCtrl(VersusController ctrl) {
        this.ctrl = ctrl;
    }

    public VersusModel() {

    }

    /**
     * This is the function that the game is based on throws an error if the board is impossible if else return the grid for the vue
     * @return an arrays of the game size that contains BlockModels for the vue to display
     * @throws OverlappedPieceException in case a piece collide with an other one
     * @throws PieceOutOfBoardException if a piece has a position outside of the board
     */
    public BlockModel[][] computeMixedGrid() throws OverlappedPieceException, PieceOutOfBoardException {
        BlockModel[][] table = new BlockModel[height][witdh];
        for (Object obj: pieceListPlayerA) {

            if(obj instanceof BlockModel) {
                BlockModel block = (BlockModel)obj;
                if(table[block.standAlonePos.y][block.standAlonePos.x] != null) throw new OverlappedPieceException();
                table[block.standAlonePos.y][block.standAlonePos.x] = block;
            }

            if(obj instanceof PieceModel) {
                PieceModel piece = (PieceModel)obj;
                for (int y = piece.y; y < piece.y+4; y++) {
                    for (int x = piece.x; x < piece.x+4; x++) {
                        if(piece.childs[y-piece.y][x-piece.x] != null) {
                            if(y> height-1 || y<0) throw new PieceOutOfBoardException();
                            if(x> witdh-1 || x<0) throw new PieceOutOfBoardException();
                            if(table[y][x] != null) throw new OverlappedPieceException();
                            table[y][x] = piece.childs[y-piece.y][x-piece.x];
                        }
                    }
                }
            }

        }
        return table;
    }

}
