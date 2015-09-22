package chess.ai;

import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;

import java.util.EnumMap;


public class MaterialMobility implements BoardEval {
	final static int MAX_VALUE = 1000;
	private EnumMap<ChessPiece,Integer> values = new EnumMap<ChessPiece,Integer>(ChessPiece.class);

	public MaterialMobility() {
		values.put(ChessPiece.BISHOP, 3);
		values.put(ChessPiece.KNIGHT, 3);
		values.put(ChessPiece.PAWN, 1);
		values.put(ChessPiece.QUEEN, 9);
		values.put(ChessPiece.ROOK, 5);
		values.put(ChessPiece.KING, MAX_VALUE);
	}

	@Override
	public int eval(Chessboard board) {
		int total = 0;
		for (BoardSquare s: board.allPieces()) {
			ChessPiece type = board.at(s);
			if (values.containsKey(type)) {
				if (board.colorAt(s).equals(board.getMoverColor())) {
					total += 100 * values.get(type);
				} else {
					total -= 100 * values.get(type);
				}
			}
		}
        total += 10 * board.getLegalMoves().size();
        total -= 5 * board.getOpponentMoveMap().makeMoveList().size();
		return total;
	}

	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	public boolean hasValue(ChessPiece piece) {
		return values.containsKey(piece);
	}
	
	public int valueOf(ChessPiece piece) {
		return values.get(piece);
	}
}
