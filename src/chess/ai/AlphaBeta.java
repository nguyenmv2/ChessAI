package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;
import chess.gui.Chess;

import java.util.ArrayList;
import java.util.List;

public class AlphaBeta extends Searcher {
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		setup(board, eval, depth);
		MoveScore result = evalMoves(board, eval, depth,-eval.maxValue(),eval.maxValue());
		tearDown();
		return result;
	}

	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		MoveScore best = null;
		for (Move m: board.getLegalMoves()) {
			Chessboard next = generate(board, m);
			MoveScore result = new MoveScore(-evalBoard(next, eval, depth - 1, -beta, -alpha), m);
			if(alpha < result.getScore()) {
				alpha = result.getScore();
				best = new MoveScore(alpha,m);
			}
			if(alpha >= beta){
				break;
			}
			if (best == null || best.getScore() < result.getScore()) best = result;
		}

		return best;

	}	
	
	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		if (depth == 0) {
			return evaluate(board, eval);
		} else {

			return evalMoves(board, eval, depth,alpha,beta).getScore();
		}
	}


}
