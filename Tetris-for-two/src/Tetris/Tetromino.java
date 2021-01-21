package Tetris;

public abstract class Tetromino {
	protected int X;
	protected int Y;
	protected int R;
	protected int W;
	protected int H;
	protected int colorType;
	protected int numOfBlockType;
	protected int[][][] block;
	
	public Tetromino() {}
	
	public void rotate() {
		R = (R+1) % numOfBlockType;
	}
	
	public void preRotate() {
		R = (R-1+numOfBlockType) % numOfBlockType;
	}

	public void moveLeft() {
		X--;
	}
	
	public void moveRight() {
		X++;
	}
	
	public void moveDown() {
		Y++;
	}
	
//	public void moverUp() {
//		Y--;
//	}
	
	public int getWidth() {
		return W;
	}
	
	public int getHeight() {
		return H;
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public int getcolorType() {
		return colorType;
	}
}

class ITetromino extends Tetromino{
	public ITetromino() {
		this.block = new int[][][] {
			{
				{1, 0, 0, 0},
				{1, 0, 0, 0},
				{1, 0, 0, 0},
				{1, 0, 0, 0}
			},
			{
				{1, 1, 1, 1},
				{0, 0, 0, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			}
		};
		numOfBlockType = 2;
		X = 3;
		Y = 0;
		R = 0;
		W = 4;
		H = 4;
		colorType = 1;
		
	}
	
	public int[][] getBlock(){
		return block[R];
	}
}