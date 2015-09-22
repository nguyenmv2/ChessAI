package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

import java.util.ArrayList;

public class SingularExtension extends Searcher {
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		setup(board, eval, depth);
		MoveScore result = evalMoves(board, eval, depth,-eval.maxValue(),eval.maxValue());
		tearDown();
		return result;
	}

	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		MoveScore best = null;
        double average = 0;
        double stdev = 0;
        ArrayList<Double> scores = new ArrayList<>();
        for (Move m: board.getLegalMoves()) {
            Chessboard next = generate(board, m);
            MoveScore result = new MoveScore(-evalBoard(next, eval, depth - 1, -beta, -alpha), m);
            average += result.getScore();
            scores.add((double)result.getScore());
        }
        average /= (double)board.getLegalMoves().size();
        for (double i: scores) {
            double newValue = Math.pow(i - average, 2);
            stdev += newValue;
        }
        stdev /= scores.size();
        stdev = Math.sqrt(stdev);

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
        if (best != null && best.getScore() > average + (3 * stdev))
            return new MoveScore(-evalBoard(generate(board, best.getMove()), eval, 1, -beta, -alpha),best.getMove());

        return best;


	}	
	
	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
        if (board.getLegalMoves().size() == 0 || board.isCheckmate()) {
                return -eval.maxValue();
        }
		if (depth == 0) {
			return evaluate(board, eval);
		} else {
            if (board.getLegalMoves().size() == 0 || board.isCheckmate()) {
                System.out.println(board.toString());
                return -eval.maxValue();
            }
			return evalMoves(board, eval, depth,alpha,beta).getScore();
		}
	}


}
