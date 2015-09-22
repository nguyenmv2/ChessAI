package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Hoa Dam on 9/22/2015.
 */
public class ABOrdering extends Searcher{
    @Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		setup(board, eval, depth);
		MoveScore result = evalMoves(board, eval, depth,-eval.maxValue(),eval.maxValue());
		tearDown();
		return result;
	}

	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		MoveScore best = null;
		List<Move> moveList = board.getLegalMoves();
		PriorityQueue<MyMoveScore> moveOrder = new PriorityQueue<>(moveList.size(), new MoveScoreComparator());
		for (Move m : board.getLegalMoves()){
			Chessboard next= generate(board, m);
			MyMoveScore score = new MyMoveScore(evaluate(next, eval),m,next);
			moveOrder.add(score);
		}

		while (!moveOrder.isEmpty()){
			MyMoveScore m = moveOrder.poll();
			MoveScore result = new MoveScore(-evalBoard(m.getBoard(), eval, depth - 1, -beta, -alpha), m.getMove());
			if(alpha < result.getScore()) {
				alpha = result.getScore();
				best = new MoveScore(alpha,m.getMove());
			}
			if(alpha >= beta){
				break;
			}
			if (best == null || best.getScore() < result.getScore()) best = result;
		}

		return best;
		}


	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		if (  board.isCheckmate()) {
			return eval.maxValue();
		}
		if (depth == 0) {
			return evaluate(board, eval);
		} else {

			return evalMoves(board, eval, depth,alpha,beta).getScore();
		}
	}

	class MoveScoreComparator implements Comparator<MyMoveScore>{
		@Override
		public int compare(MyMoveScore x, MyMoveScore y) {
			if(x.getScore() < y.getScore()) return -1;
			if(x.getScore() > y.getScore()) return 1;
			return 0;
		}
	}


}
