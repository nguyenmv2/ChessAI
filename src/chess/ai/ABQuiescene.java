package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

import java.util.ArrayList;
import java.util.List;

public class ABQuiescene extends Searcher {
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
            if (best == null || best.getScore() < result.getScore()) best = result;
			if(alpha >= beta){
				break;
			}

		}

		return best;

	}	
	
	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		if (  board.isCheckmate()) {

			System.out.println("Illegal board");
			System.out.println(board.toString());
			return eval.maxValue();
		}
		if (depth == 0) return quiescene(board, eval, alpha, beta);
		else {
			return evalMoves(board, eval, depth,alpha,beta).getScore();
		}
	}
	int quiescene(Chessboard board, BoardEval eval, int alpha, int beta){
		int standingVal = evaluate(board, eval);
		if (standingVal >= beta) return beta;
		if (standingVal > alpha) alpha = standingVal;
		List<Move> capturable = new ArrayList<>();
		for (Move m: board.getLegalMoves()){
			if(m.getCapture() != null) capturable.add(m);
		}
		if (capturable.size() == 0 ) return alpha;
		for (Move mv : capturable){
			Chessboard next = generate(board, mv);
			int score = quiescene(next, eval, -beta, -alpha);
			if (score >= beta) return beta;
			if (score > alpha) alpha = score;
		}
		return alpha;
	}

}
