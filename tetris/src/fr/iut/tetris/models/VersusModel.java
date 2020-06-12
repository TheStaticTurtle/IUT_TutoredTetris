package fr.iut.tetris.models;

import fr.iut.tetris.Log;
import fr.iut.tetris.controllers.VersusController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.enums.LineCompleted;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class VersusModel {

    public int height = 20;
    public int width = 10;
    public int fallSpeedPlayerA = 1000; //ms
    public int fallSpeedPlayerB = 1000; //ms
    ArrayList<Object> pieceListPlayerA = new ArrayList<>();
    ArrayList<Object> pieceListPlayerB = new ArrayList<>();
    public ArrayList<EffectModel> effectListPlayerA = new ArrayList<>();
    public ArrayList<EffectModel> effectListPlayerB = new ArrayList<>();
    public GameState gameState = GameState.WAITING;
    public PieceModel fallingPiecePlayerA = null;
    public PieceModel fallingPiecePlayerB = null;
    public PieceModel nextPiecePlayerA;
    public PieceModel nextPiecePlayerB;
    VersusController ctrl;
    public int bestScore = 0;
    public int currentScorePlayerA = 0;
    public int currentScorePlayerB = 0;
    public String winner;
    Random rand = new Random();

    public VersusModel() {
        nextPiecePlayerA = getRandomPiece(0);
        nextPiecePlayerB = getRandomPiece(1);

        effectListPlayerA.add(new MalusSpeed());
        effectListPlayerA.add(new RandomLine(this,0));
        effectListPlayerA.add(new InvertControls(this,0,30));
    }

    public void setCtrl(VersusController ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Fetch a random element from a list
     * @param list the list of objects
     * @param rand the Random class
     * @return a random object
     */
    static Object getRandomElement(Object[] list, Random rand)  {
        return list[rand.nextInt(list.length)];
    }

    /**
     * Return a random pieces from PieceModel.Pieces and colors it according to which player
     * @return a PieceModel
     */
    public PieceModel getRandomPiece(int player) { //0=PlayerA 1=PlayerB
        Log.info(this,"Spawned a new random piece");
        return ((PieceModel)getRandomElement(PieceModel.getPieces(),rand)).clone();
    }

    /**
     * Spawn a new piece for the 1st player. Transfer the `nextPiecePlayerA` to `fallingPiecePlayerA` and generate a new piece for `nextPiecePlayerA`
     */
    public void spawnPlayerAPiece() {
        if(fallingPiecePlayerA == null) {
            PieceModel p = nextPiecePlayerA.clone();
            pieceListPlayerA.add(p);
            fallingPiecePlayerA = p;
            nextPiecePlayerA = getRandomPiece(0);
            this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_SPAWN_PLAYER_A"));
        }
        try {
            computeMixedGrid(0);
        } catch (OverlappedPieceException | PieceOutOfBoardException e) {
            winner = "B";
            gameState = GameState.FINISHED;
            /*this.bestScore = this.ctrl.gameEnded();*/
        }
    }

    /**
     * Spawn a new piece for the 2nd player. Transfer the `nextPiecePlayerB` to `fallingPiecePlayerB` and generate a new piece for `nextPiecePlayerB`
     */
    public void spawnPlayerBPiece() {
        if(fallingPiecePlayerB == null) {
            PieceModel p = nextPiecePlayerB.clone();
            pieceListPlayerB.add(p);
            fallingPiecePlayerB = p;
            nextPiecePlayerB = getRandomPiece(1);
            this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_SPAWN_PLAYER_B"));
        }

        try {
            computeMixedGrid(1);
        } catch (OverlappedPieceException | PieceOutOfBoardException e) {
            winner = "A";
            gameState = GameState.FINISHED;
            /*this.bestScore = this.ctrl.gameEnded();*/
        }
    }

    /**
     * This is the function that the game is based on throws an error if the board is impossible if else return the grid for the vue
     * @return an arrays of the game size that contains BlockModels for the vue to display
     * @throws OverlappedPieceException in case a piece collide with an other one
     * @throws PieceOutOfBoardException if a piece has a position outside of the board
     */
    public BlockModel[][] computeMixedGrid(int player) throws OverlappedPieceException, PieceOutOfBoardException {
        BlockModel[][] table = new BlockModel[height][width];

        ArrayList<Object> pieceList;
        if (player == 0) { pieceList = pieceListPlayerA; }
        else { pieceList = pieceListPlayerB; }

        for (Object obj: pieceList) {

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
                            if(x> width -1 || x<0) throw new PieceOutOfBoardException();
                            if(table[y][x] != null) throw new OverlappedPieceException();
                            table[y][x] = piece.childs[y-piece.y][x-piece.x];
                        }
                    }
                }
            }

        }
        return table;
    }

    /**
     * A recursive function that check for line that are completed and falls down every line over it then recursively calls itself with the parm `firstCall` at true which make the function return an integer instead of a LineCompleted
     * @param firstCall In the program should be set to false only used by the function for the socre multiplier calculation
     * @return When use in the program return LineCompleted if not it return an Integer of the umber of line removed
     */
    Object checkForFullLineAndRemoveIt(boolean firstCall, int player){
        try {
            BlockModel[][] grid = computeMixedGrid(player);
            int firstLineY = 0;
            Integer lineCount = 0;

            if (player == 0) { currentScorePlayerA += 4; }
            else { currentScorePlayerB += 4; }
            // Difficulty
            if (player == 0) {
                if(fallSpeedPlayerA > 75) {
                    fallSpeedPlayerA = (int)(1000 - 0.3*currentScorePlayerA);
                    Log.debug(this, "FallSpeed Player A = " + this.fallSpeedPlayerA);
                }
            } else {
                if(fallSpeedPlayerB > 75) {
                    fallSpeedPlayerB = (int)(1000 - 0.3*currentScorePlayerB);
                    Log.debug(this, "FallSpeed Player B = " + this.fallSpeedPlayerB);
                }
            }

            for (int y = grid.length-1; y >= 0; y--) {
                boolean isLineFull = true;
                for (BlockModel block: grid[y]) {
                    if (block == null) {isLineFull = false; break;}
                }

                if(isLineFull) {
                    lineCount += 1;
                    firstLineY = y;
                    Log.info(this,"Line "+y+" is full");

                    for (BlockModel block: grid[y]) {
                        grid[y] = null;
                        if (player == 0) { pieceListPlayerA.remove(block); }
                        else { pieceListPlayerB.remove(block); }
                    }

                    for (int fally = y-1; fally >= 0; fally--) {
                        for (BlockModel block: grid[fally]) {
                            if(block != null) {
                                Log.debug(this, "Falling line " + fally + " to "+(fally+1)+" (x:"+block.standAlonePos.x+")");
                                block.standAlonePos.y = Math.min( (fally+1) , this.height - 1);
                            }
                        }
                    }
                    // Line has fallen down recheck to see if there is more lines but tel the function to spit out an integer instead of the LineCompleted enum
                    lineCount += (Integer)checkForFullLineAndRemoveIt(false, player);
                    break;
                }
            }

            if(firstCall) {
                return LineCompleted.getScore(lineCount,firstLineY,this.height);
            } else {
                return lineCount;
            }
        } catch (PieceOutOfBoardException | OverlappedPieceException ignored) {}
        return LineCompleted.NO_LINE;
    }

    /**
     * When a piece has fallen down to it's maximum it needs to be converted to individual blocks so that we can adjust individuals Y's of blocks instead of moving the whole piece (useful when a piece is cut in the middle for example)
     * @param piece the piece to transform
     */
    void convertFullPiecesToBlocks(PieceModel piece, int player) {
        Log.info(this,"Converting the fallling piece to individual blocks");
        if (player == 0) { pieceListPlayerA.remove(piece); }
        else { pieceListPlayerB.remove(piece); }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(piece.childs[y][x] != null) {
                    BlockModel block = piece.childs[y][x];
                    if(block != null) {
                        block.standAlonePos = new Point(piece.x +x, piece.y+y);
                        if (player == 0) { pieceListPlayerA.add(block.clone()); }
                        else { pieceListPlayerB.add(block.clone()); }
                    }
                }
            }
        }
    }

    /**
     * Move the current piece for the correct player in the X axis also send a "GAME:FAILED_ACTION" event to the controller if the movement is impossible
     * @param dir the direction
     * @return if the movment was successful
     */
    public boolean moveCurrentX(int player, Direction dir) {
        if(player==0) {
            if(fallingPiecePlayerA != null) {
                fallingPiecePlayerA.x += dir.step;
                try {
                    computeMixedGrid(0);
                    return true;
                } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                    fallingPiecePlayerA.x -= dir.step;
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
                    return false;
                }
            }
            return false;
        }
        if(player==1) {
            if(fallingPiecePlayerB != null) {
                fallingPiecePlayerB.x += dir.step;
                try {
                    computeMixedGrid(1);
                    return true;
                } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                    fallingPiecePlayerB.x -= dir.step;
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Rotate the current piece for the correct player also send a "GAME:FAILED_ACTION" event to the controller if the rotation is impossible
     * @param dir the direction of the rotation
     * @return if the rotation was successful
     */
    public boolean rotateCurrent(int player, Direction dir) {
        if(player==0) {
            if(fallingPiecePlayerA != null) {
                fallingPiecePlayerA.rotateModel(dir.step, fallingPiecePlayerA.name);
                try {
                    computeMixedGrid(0);
                    return true;
                } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                    fallingPiecePlayerA.rotateModel(dir.step * -1, fallingPiecePlayerA.name);
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
                    return false;
                }
            }
            return false;
        }
        if(player==1) {
            if(fallingPiecePlayerB != null) {
                fallingPiecePlayerB.rotateModel(dir.step, fallingPiecePlayerB.name);
                try {
                    computeMixedGrid(1);
                    return true;
                } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                    fallingPiecePlayerB.rotateModel(dir.step * -1, fallingPiecePlayerB.name);
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Fall the current piece down by one for the player A. Also test if the fall is possible if else the piece get converted to blocks and we check for a full line, we send a event to the controller for sound effects and we calculate the score
     */
    public void fallCurrentForPlayerA() {
        if(fallingPiecePlayerA != null) {
            fallingPiecePlayerA.y++;
            try {
                computeMixedGrid(0);
            } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                fallingPiecePlayerA.y--;
                convertFullPiecesToBlocks(fallingPiecePlayerA, 0);
                LineCompleted score = (LineCompleted)checkForFullLineAndRemoveIt(true, 0);
                this.calculateScore(score, 0);
                if(score == LineCompleted.QUAD_LINE || score == LineCompleted.BOTTOM_QUAD_LINE) {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FOURLINES_COMPLETE"));
                } else if(score == LineCompleted.NO_LINE ) {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_PLACE"));
                } else {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:LINE_COMPLETE"));
                }
                Log.info(this,"Got score: "+score.toString());

                fallingPiecePlayerA = null; // The piece can not fall anymore
            }
        }
    }
    /**
     * Fall the current piece down by one for the player B. Also test if the fall is possible if else the piece get converted to blocks and we check for a full line, we send a event to the controller for sound effects and we calculate the score
     */
    public void fallCurrentForPlayerB() {
        if(fallingPiecePlayerB != null) {
            fallingPiecePlayerB.y++;
            try {
                computeMixedGrid(1);
            } catch (PieceOutOfBoardException | OverlappedPieceException e) {
                fallingPiecePlayerB.y--;
                convertFullPiecesToBlocks(fallingPiecePlayerB, 2);
                LineCompleted score = (LineCompleted)checkForFullLineAndRemoveIt(true, 1);
                this.calculateScore(score, 1);
                if(score == LineCompleted.QUAD_LINE || score == LineCompleted.BOTTOM_QUAD_LINE) {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FOURLINES_COMPLETE"));
                } else if(score == LineCompleted.NO_LINE ) {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_PLACE"));
                } else {
                    this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:LINE_COMPLETE"));
                }
                Log.info(this,"Got score: "+score.toString());

                fallingPiecePlayerB = null; // The piece can not fall anymore
            }
        }
    }

    /**
     * Execute fallCurrentForPlayerA() until the `fallingPiecePlayerA` is no more
     */
    public void fallCurrentAtBottomForPlayerA() {
        while(fallingPiecePlayerA != null) {
            fallCurrentForPlayerA();
        }
    }
    /**
     * Execute fallCurrentForPlayerB() until the `fallingPiecePlayerB` is no more
     */
    public void fallCurrentAtBottomForPlayerB() {
        while(fallingPiecePlayerB != null) {
            fallCurrentForPlayerB();
        }
    }

    /**
     * Calculate the current score based of the point multiplier of the LineCompleted parameter
     * @param lc what multiplier should we use
     */
    public void calculateScore(LineCompleted lc, int player) {
        if (player == 0) { this.currentScorePlayerA += 10 * lc.pointMultiplier; }
        else { this.currentScorePlayerB += 10 * lc.pointMultiplier; }
    }

}
