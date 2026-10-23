package bundle.dao;

/**
 * DAOでスローされる例外を統一して扱うための独自例外
 */
public class DAOException extends Exception {

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

}
