package tetris;

public abstract class TetrisGameState {
    protected ITetris tetris;
    
    public TetrisGameState() {
    }

    public void init() {

    }

    public void rotate() {
        // TODO implement here
    }

    public void moveLeft() {
        // TODO implement here
    }

    public void moveRight() {
        // TODO implement here
    }

    public void moveDown() {
        // TODO implement here
    }

    public void fixCurrentBlock() {

    }

    public void moveBottom() {
        // TODO implement here
    }

    public void updateBlock() {
    }

    public boolean gameOver() {
        return false;
    }

    public void updateBoard() {

    }

    public Tetromino getCurrentTetrominos() {
        return null;
    }

    public Tetromino getNextTetrominos() {
        return null;
    }

    public Tetromino getShodowTetrominos() {
        return null;
    }

    public void update() {
        TetrisLog.d("TetrisGameState.update()");
        if (tetris != null) {
            tetris.getObserver().update();
        }
    }

    public boolean isIdleState() { return false; }
    public boolean isGameOverState() { return false; }
    public boolean isPlayState() { return false; }
    public boolean isPauseState() { return false; }
}

class TetrisIdleState extends TetrisGameState {
    public TetrisIdleState(Tetris tetris) {
        TetrisLog.d("TetrisIdleState()");
        this.tetris = tetris;
    }
    public boolean isIdleState() {
       return true;
    }
}

class TetrisPlayState extends TetrisGameState {
    private Tetromino currentTetrominos;
    private Tetromino nextTetrominos;
    private Tetromino shadowTetrominos;
    private TetrisBoard tetrisBoard;
    private int additionalPoint = 1;

    public TetrisPlayState(Tetris tetris, TetrisBoard board) {
        this.tetris = tetris;
        this.tetrisBoard = board;
        currentTetrominos = TetrominosFactory.create();
        nextTetrominos = TetrominosFactory.create();
        shadowTetrominos = TetrominosFactory.clone(currentTetrominos);
    }

    public void init() {
        this.tetrisBoard.init();
        currentTetrominos = TetrominosFactory.create();
        shadowTetrominos = TetrominosFactory.clone(currentTetrominos);
        nextTetrominos = TetrominosFactory.create();
        additionalPoint = 1;
    }

    public void moveLeft() {
       TetrisLog.d("TetrisPlayState.moveLeft()");
        currentTetrominos.moveLeft();
        if (tetrisBoard.isAcceptable(currentTetrominos) == false) {
            currentTetrominos.moveRight();
            TetrisLog.d("Not Accept");
        }
    }

    public void moveRight() {
        TetrisLog.d("TetrisPlayState.moveRight()");
        currentTetrominos.moveRight();
        if (tetrisBoard.isAcceptable(currentTetrominos) == false) {
            currentTetrominos.moveLeft();
            TetrisLog.d("Not Accept");
        }
    }

    public void rotate() {
        TetrisLog.d("TetrisPlayState.rotate()");
        currentTetrominos.rotate();
        if (tetrisBoard.isAcceptable(currentTetrominos) == false) {
            currentTetrominos.preRotate();
            TetrisLog.d("Not Accept");
        }
    }


    public void moveDown() {
        TetrisLog.d("TetrisPlayState.moveDown()");
        currentTetrominos.moveDown();
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            TetrisLog.d("Accept");
        } else {
            currentTetrominos.moveUp();
            TetrisLog.d("Can not move down");
            fixCurrentBlock();
            updateBoard();
            updateBlock() ;
        }
    }


    public void moveBottom() {
        TetrisLog.d("TetrisPlayState.moveBottom()");
        while(tetrisBoard.isAcceptable(currentTetrominos)) {
            currentTetrominos.moveDown();
        }
        if (tetrisBoard.isAcceptable(currentTetrominos)) {
            return;
        }
        currentTetrominos.moveUp();
    }


    public void fixCurrentBlock() {
        tetrisBoard.addTetromino(currentTetrominos);
    }

    public void updateBlock() {
        currentTetrominos = nextTetrominos;
        shadowTetrominos = TetrominosFactory.clone(currentTetrominos);
        nextTetrominos = TetrominosFactory.create();
    }

    public boolean gameOver() {
        TetrisLog.d("Check Game over");
        return !tetrisBoard.isAcceptable(currentTetrominos);
    }

    public void updateBoard() {
        int removedLine = tetrisBoard.arrange();
        int point = calculatorScore(removedLine);
        tetris.addSore(point);
        tetris.setLevel();
    }

    private int calculatorScore(int removedLineCount) {
        if (removedLineCount == 0) {
            additionalPoint = 1;
            return 0;
        }

        int lineScore = 12 * removedLineCount;

        if (additionalPoint > 10000) {
            additionalPoint = 10000;
        }
        additionalPoint <<= removedLineCount;
        TetrisLog.d("calculatorScore : " + tetris.getLevel() + " * 10 * " + additionalPoint + " + " + lineScore);
        return  (tetris.getLevel() * 10 * additionalPoint + lineScore);
    }

    public Tetromino getCurrentTetrominos() {
        return currentTetrominos;
    }

    public Tetromino getNextTetrominos() {
        return nextTetrominos;
    }

    public Tetromino getShodowTetrominos() {
        moveShadowBottom();
        return shadowTetrominos;
    }

    public void moveShadowBottom() {
        TetrisLog.d("TetrisPlayState.moveShadowBottom()");

        shadowTetrominos.clone(currentTetrominos);

        while(tetrisBoard.isAcceptable(shadowTetrominos)) {
            shadowTetrominos.moveDown();
        }
        if (tetrisBoard.isAcceptable(shadowTetrominos)) {
            return;
        }
        shadowTetrominos.moveUp();
    }

    public boolean isPlayState() {
        return true;
    }
}

class TetrisPauseState extends TetrisGameState {
    public TetrisPauseState(Tetris tetris) {
        this.tetris = tetris;
    }

    public boolean isPauseState() { return true; }
}

class TetrisGameOverState extends TetrisGameState {
    public TetrisGameOverState(Tetris tetris) {
        this.tetris = tetris;
    }

    public boolean isGameOverState() {
        return true;
    }
}