package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

/**
 * Created by Hoa Dam on 9/22/2015.
 */
public class MyMoveScore {
    private Move move;
	private int score;
	private boolean cutoff;
	private double distance;
	private Chessboard board;

	public MyMoveScore(int score, Move m,Chessboard board) {
		this.move = m;
		this.score = score;
		this.cutoff = false;
		this.board = board;
	}

	public void setCutoff(boolean cutoff) {this.cutoff = cutoff;}
	public void setDistance(double distance) {this.distance = distance;}

	public Chessboard getBoard() {return board;}
	public int getScore() {return score;}
	public Move getMove() {return move;}
	public boolean isCutoff() {return cutoff;}
	public double getDistance() {return distance;}
}
