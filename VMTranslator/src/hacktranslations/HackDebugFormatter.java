package hacktranslations;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class HackDebugFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		return record.getMessage();
	}

}
